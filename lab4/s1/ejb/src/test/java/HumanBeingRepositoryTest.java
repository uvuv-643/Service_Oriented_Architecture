import org.junit.jupiter.api.Test;
import ru.uvuv643.ejb.remote.dto.enums.*;
import ru.uvuv643.ejb.remote.dto.human.CarDto;
import ru.uvuv643.ejb.remote.dto.human.CoordinatesDto;
import ru.uvuv643.ejb.remote.dto.human.HumanBeingDto;
import ru.uvuv643.ejb.remote.dto.human.request.CoordinatesUpdateDto;
import ru.uvuv643.ejb.remote.dto.human.request.CreateHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.request.ModifyHumanBeingRequest;
import ru.uvuv643.ejb.remote.dto.human.response.AllResponseDto;
import ru.uvuv643.ejb.remote.repositories.HumanBeingRepository;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class HumanBeingRepositoryTest {

    private final int EXISTING_ID = 3;

    @Test
    public void createHumanBeing() {
        HumanBeingRepository repository = new HumanBeingRepository();
        assertDoesNotThrow(() -> repository.createHumanBeing(new CreateHumanBeingRequest(
                "null",
                new CoordinatesDto(1, 2L),
                true,
                false,
                1.0d,
                1.0,
                new CarDto(true),
                MoodDto.APATHY,
                WeaponTypeDto.HAMMER
        )));
    }

    @Test
    public void modifyHumanBeing() {
        HumanBeingRepository repository = new HumanBeingRepository();
        assertDoesNotThrow(() -> repository.modifyHumanBeingById(EXISTING_ID, new ModifyHumanBeingRequest(
                "not null", new CoordinatesUpdateDto(2, 1L), true, true,
                2.0d, 2.3d, new CarDto(false), MoodDto.SORROW,
                WeaponTypeDto.AXE
        )));
    }

    @Test
    public void modifyHumanBeingOnlyName() {
        HumanBeingRepository repository = new HumanBeingRepository();
        assertDoesNotThrow(() -> repository.modifyHumanBeingById(EXISTING_ID, new ModifyHumanBeingRequest(
                "modified!", null, null, null, null, null, null, null, null
        )));
    }

    @Test
    public void getHumanBeingStats() throws SQLException {
        HumanBeingRepository repository = new HumanBeingRepository();
        double minValue = repository.getHumanStats(StatisticFieldDto.IMPACT_SPEED, StatisticOperationDto.MIN);
        double averageValue = repository.getHumanStats(StatisticFieldDto.IMPACT_SPEED, StatisticOperationDto.AVERAGE);
        double maxValue = repository.getHumanStats(StatisticFieldDto.IMPACT_SPEED, StatisticOperationDto.MAX);
        System.out.println(minValue + ", " + averageValue + ", " + maxValue);
        assert minValue <= averageValue && averageValue <= maxValue;
    }

    @Test
    public void getHumanBeingById() {
        HumanBeingRepository repository = new HumanBeingRepository();
        assertDoesNotThrow(() -> {
            HumanBeingDto humanBeing = repository.getHumanBeingById(EXISTING_ID);
            System.out.println(humanBeing);
            assert humanBeing.getId() == EXISTING_ID;
        });
    }

    @Test
    public void getAllHumanBeings() {
        HumanBeingRepository repository = new HumanBeingRepository();
        assertDoesNotThrow(() -> {
            AllResponseDto response = repository.getAllHumanBeing(3, 4, null, null, null, null, null, null, null, true, null, null, null, null, null, null, null, null, 1, 1, List.of("realHero", "id"), List.of(SortOrderDto.ASC, SortOrderDto.ASC));
            System.out.println(response);
        });
    }

    @Test
    public void getAllHumanBeingsFilteredByDate() {
        HumanBeingRepository repository = new HumanBeingRepository();
        assertDoesNotThrow(() -> {
            AllResponseDto response = repository.getAllHumanBeing(null, null, null, null, null, null, null, "2024-11-01", "2025-12-01", true, null, null, null, null, null, null, null, null, 1, 1, List.of("realHero", "id"), List.of(SortOrderDto.ASC, SortOrderDto.ASC));
            System.out.println(response);
        });
    }


    @Test
    public void deleteByParams() {
        HumanBeingRepository repository = new HumanBeingRepository();
        assertDoesNotThrow(() -> {
            repository.deleteByParams(null, true, null, null);
            AllResponseDto response = repository.getAllHumanBeing(0, 15, null, null, null, null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, List.of("realHero", "id"), List.of(SortOrderDto.ASC, SortOrderDto.ASC));
            assert response.getHumanBeingDto().size() == 1; // supposed only id = 3 left
        });
    }

}
