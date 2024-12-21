/* eslint-disable complexity */
/* eslint-disable import/no-extraneous-dependencies */
import {useEffect, useMemo, useState} from 'react';
import {Wrapper} from './components/Wrapper';

import {Alert, Button, Dialog, Select, Switch, Table, TextInput} from '@gravity-ui/uikit';
import axios from 'axios';
import {CircleXmarkFill, LockOpen, PencilToSquare, PersonPlanetEarth} from '@gravity-ui/icons';
import {Buffer} from 'buffer';
import {
    TeamMap,
    TeamMember,
    convertToJsonXml,
    parseAllRequestFromXml,
    parseTeamResponse,
} from './utils/xmlResponseParser';
import {isDouble, isInteger, isMaxValue, isMinValue, isValidDate} from './utils/validation';
import {xml2js} from 'xml-js';

// @ts-ignore
window.Buffer = Buffer;

// Типы данных
type FilterFormData = {
    idGte: string | null;
    idLte: string | null;
    nameIn: string;
    coordinateXGte: string | null;
    coordinateXLte: string | null;
    coordinateYGte: string | null;
    coordinateYLte: string | null;
    creationDateGte: string | null;
    creationDateLte: string | null;
    realHero: boolean | null;
    hasToothpick: boolean | null;
    impactSpeedGte: string | null;
    impactSpeedLte: string | null;
    minutesOfWaitingGte: string | null;
    minutesOfWaitingLte: string | null;
    coolCar: boolean | null;
    moodIn: string[];
    weaponTypeIn: string[];
    page: string | null;
    size: string | null;
    sortFields: (string | null)[];
    sortDirections: ('ASC' | 'DESC' | null)[];
};

const moodOptionsTranslate = {
    SORROW: 'Печальный',
    APATHY: 'Апатия',
    CALM: 'Спокойный',
    RAGE: 'Ярость',
    FRENZY: 'Бешенство',
};
const weaponTypeOptionsTranslate = {
    HAMMER: 'Молоток',
    AXE: 'Топор',
    PISTOL: 'Пистолет',
    KNIFE: 'Нож',
    BAT: 'Бита',
};

const App = () => {
    const [data, setData] = useState<TeamMember[]>([]);

    const columns = [
        {id: 'id', name: 'ИД'},
        {id: 'name', name: 'Имя'},
        {id: 'coordinates', name: 'Координаты'},
        {id: 'creationDate', name: 'Создан'},
        {id: 'realHero', name: 'Герой?'},
        {id: 'hasToothpick', name: 'Зубная щетка?'},
        {id: 'impactSpeed', name: 'Скорость'},
        {id: 'minutesOfWaiting', name: 'Ожидание'},
        {id: 'weaponType', name: 'Оружие'},
        {id: 'mood', name: 'Настроение'},
        {id: 'carCool', name: 'Крутая тачка?'},
        {id: 'teams', name: 'Команды'},
        {id: 'actions', name: ''},
    ];

    type TeamMemberCreate = {
        name: string;
        coordinates: {
            x: string;
            y: string;
        };
        realHero: boolean;
        hasToothpick: boolean;
        impactSpeed: string;
        minutesOfWaiting: string;
        weaponType: string | null;
        mood: string;
        carCool: boolean;
    };

    const [creating, setCreating] = useState<TeamMemberCreate>({
        name: '',
        coordinates: {
            x: '',
            y: '',
        },
        realHero: false,
        hasToothpick: false,
        impactSpeed: '',
        minutesOfWaiting: '',
        weaponType: 'BAT',
        mood: 'CALM',
        carCool: false,
    });

    type TeamMembersKeys = {
        [key in keyof TeamMemberCreate]?: string;
    } & {
        coordinatesX?: string;
        coordinatesY?: string;
    };

    const [creatingErrors, setCreatingErrors] = useState<TeamMembersKeys>({});

    const [addPopupOpen, setAddPopupOpen] = useState<boolean>(false);
    const [statsPopupOpen, setStatsPopupOpen] = useState<boolean>(false);
    const [deletePopupOpen, setDeletePopupOpen] = useState<boolean>(false);
    const [sadPopupOpen, setSadPopupOpen] = useState<boolean>(false);
    const [modifyPopupOpen, setModifyPopupOpen] = useState<boolean>(false);
    const [teamPopupOpen, setTeamPopupOpen] = useState<boolean>(false);

    const [modifyObject, setModifyObject] = useState<TeamMember>();
    const [team, setTeam] = useState<string>('');

    const [teamData, setTeamData] = useState<TeamMap>({});

    useEffect(() => {
        axios.get('https://rwfsh39.ru/s2/api/v1/team/heroes').then((response) => {
            const parsedResponse = parseTeamResponse(response.data);
            setTeamData(parsedResponse);
        });
    }, [data]);

    const [deleteRequestData, setDeleteRequestData] = useState<TeamMember>();
    const [openDeleteConfirmPopup, setOpenDeleteConfirmPopup] = useState<boolean>(false);

    const tableData = useMemo(() => {
        return data.map((member) => ({
            id: member.id,
            name: member.name,
            coordinates: `(${member.coordinates.x}; ${member.coordinates.y})`,
            creationDate:
                new Date(member.creationDate).getDate() +
                '.' +
                new Date(member.creationDate).getMonth() +
                '.' +
                new Date(member.creationDate).getFullYear(),
            realHero: member.realHero ? 'Да' : 'Нет',
            hasToothpick: member.hasToothpick ? 'Да' : 'Нет',
            impactSpeed: member.impactSpeed,
            minutesOfWaiting: member.minutesOfWaiting,
            weaponType: (weaponTypeOptionsTranslate as any)[member.weaponType ?? ''],
            mood: (moodOptionsTranslate as any)[member.mood ?? ''],
            carCool: member.carCool ? 'Да' : 'Нет',
            teams: teamData[member.id]?.join('; '),
            actions: (
                <div
                    style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        width: 90,
                    }}
                >
                    <div style={{cursor: 'pointer'}}>
                        <PersonPlanetEarth
                            width={20}
                            height={20}
                            onClick={() => {
                                setModifyObject(member);
                                setTeamPopupOpen(true);
                            }}
                        />
                    </div>
                    <div style={{cursor: 'pointer'}}>
                        <PencilToSquare
                            width={20}
                            height={20}
                            onClick={() => {
                                setModifyObject(member);
                                setCreating({
                                    name: member.name,
                                    coordinates: {
                                        x: member.coordinates.x,
                                        y: member.coordinates.y,
                                    },
                                    realHero: member.realHero,
                                    hasToothpick: member.hasToothpick,
                                    impactSpeed: member.impactSpeed,
                                    minutesOfWaiting: member.minutesOfWaiting,
                                    weaponType: member.weaponType,
                                    mood: member.mood,
                                    carCool: member.carCool,
                                });
                                setModifyPopupOpen(true);
                            }}
                        />
                    </div>
                    <div
                        style={{cursor: 'pointer'}}
                        onClick={() => {
                            setDeleteRequestData(member);
                            setOpenDeleteConfirmPopup(true);
                            console.log('!@#');
                        }}
                    >
                        <div></div>
                    </div>
                </div>
            ),
        }));
    }, [data, teamData]);

    const [filters, setFilters] = useState<FilterFormData>({
        idGte: null,
        idLte: null,
        nameIn: '',
        coordinateXGte: null,
        coordinateXLte: null,
        coordinateYGte: null,
        coordinateYLte: null,
        creationDateGte: null,
        creationDateLte: null,
        realHero: null,
        hasToothpick: null,
        impactSpeedGte: null,
        impactSpeedLte: null,
        minutesOfWaitingGte: null,
        minutesOfWaitingLte: null,
        coolCar: null,
        moodIn: [],
        weaponTypeIn: [],
        page: '1',
        size: '10',
        sortFields: ['id'],
        sortDirections: ['ASC'],
    });

    type FilterKeys = {
        [key in keyof FilterFormData]?: string;
    };

    const [filterErrors, setFilterErrors] = useState<FilterKeys>({});

    const handleFilterUpdate = (e: boolean | null, field: string) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            [field]: e,
        }));
    };

    const handleFilterUpdateStringA = (e: string[], field: string) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            [field]: e,
        }));
    };

    const handleFilterUpdateStringInd = (
        e: string[],
        field: 'sortFields' | 'sortDirections',
        index: number,
    ) => {
        const targetField = JSON.parse(JSON.stringify(filters[field])) as string[];
        targetField[index] = e[0];
        setFilters((prevFilters) => ({
            ...prevFilters,
            [field]: targetField,
        }));
    };

    const expandSort = () => {
        const targetField1 = JSON.parse(JSON.stringify(filters['sortDirections'])) as (
            | 'ASC'
            | 'DESC'
            | null
        )[];
        const targetField2 = JSON.parse(JSON.stringify(filters['sortFields'])) as (string | null)[];
        targetField1.push(null);
        targetField2.push(null);

        setFilters((prevFilters) => ({
            ...prevFilters,
            ['sortDirections']: targetField1,
            ['sortFields']: targetField2,
        }));
    };

    const handleFilterChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
        field: string,
    ) => {
        let value = null;
        if (typeof e === 'boolean') {
            value = e;
        } else {
            value = e.target.value;
        }
        setFilters((prevFilters) => ({
            ...prevFilters,
            [field]: value,
        }));
    };

    const handleCreatingChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
        field: string,
    ) => {
        const value = e.target.value;
        setCreating((prevCreating) => {
            if (field === 'coordinatesX') {
                return {
                    ...prevCreating,
                    ['coordinates']: {
                        x: value,
                        y: prevCreating.coordinates.y,
                    },
                };
            }
            if (field === 'coordinatesY') {
                return {
                    ...prevCreating,
                    ['coordinates']: {
                        x: prevCreating.coordinates.x,
                        y: value,
                    },
                };
            }
            return {
                ...prevCreating,
                [field]: value,
            };
        });
    };

    const handleCreatingChangeS = (e: any, field: string) => {
        setCreating((prevCreating) => ({
            ...prevCreating,
            [field]: e,
        }));
    };

    // useEffect(() => {
    //     console.log(filters);
    // }, [filters]);

    const [sendRequestCount, setSendRequestCount] = useState<number>(0);
    const [successAdded, setSuccessAdded] = useState<boolean>(false);

    const handleSubmitFilters = () => {
        const errorObject: FilterKeys = {};

        if (!isInteger(filters.idLte)) errorObject['idLte'] = 'Должно быть целым числом';
        if (!isInteger(filters.idGte)) errorObject['idGte'] = 'Должно быть целым числом';

        if (!isInteger(filters.coordinateXGte))
            errorObject['coordinateXGte'] = 'Должно быть целым числом';
        if (!isInteger(filters.coordinateXLte))
            errorObject['coordinateXLte'] = 'Должно быть целым числом';

        if (!isInteger(filters.coordinateYGte))
            errorObject['coordinateYGte'] = 'Должно быть целым числом';
        if (!isInteger(filters.coordinateYGte))
            errorObject['coordinateYGte'] = 'Должно быть целым числом';

        if (!isValidDate(filters.creationDateGte))
            errorObject['creationDateGte'] = 'Должно быть датой';
        if (!isValidDate(filters.creationDateLte))
            errorObject['creationDateLte'] = 'Должно быть датой';

        if (!isDouble(filters.impactSpeedGte)) errorObject['impactSpeedGte'] = 'Должно быть числом';
        if (!isDouble(filters.impactSpeedLte)) errorObject['impactSpeedLte'] = 'Должно быть числом';

        if (!isDouble(filters.minutesOfWaitingGte))
            errorObject['minutesOfWaitingGte'] = 'Должно быть числом';
        if (!isDouble(filters.minutesOfWaitingLte))
            errorObject['minutesOfWaitingLte'] = 'Должно быть числом';

        if (!isInteger(filters.page)) errorObject['page'] = 'Должно быть целым числом';
        if (!isMinValue(filters.page, 1)) errorObject['page'] = 'Должно быть положительным числом';
        if (!isMaxValue(filters.page, 110010))
            errorObject['page'] = 'Должно быть положительным числом';

        setFilterErrors(errorObject);
        if (Object.keys(errorObject).length === 0) {
            setSendRequestCount(sendRequestCount + 1);
        }
    };

    const handleSubmitDelete = () => {
        const errorObject: TeamMembersKeys = {};
        if (!isDouble(creating.impactSpeed))
            errorObject['impactSpeed'] = 'Должно быть целым числом';

        if (!isInteger(creating.minutesOfWaiting))
            errorObject['minutesOfWaiting'] = 'Должно быть целым числом';

        if (!['+', '-', ''].includes(creating.name)) {
            errorObject['name'] = 'Должно быть +, - или пустое';
        }

        if (Object.keys(errorObject).length === 0) {
            axios
                .delete('https://rwfsh39.ru/s1/api/v1/human-being/', {
                    params: {
                        carCool:
                            // eslint-disable-next-line no-nested-ternary
                            creating.name === '+'
                                ? true
                                : creating.name === '-'
                                  ? false
                                  : undefined,
                        impactSpeed: creating.impactSpeed.length ? creating.impactSpeed : undefined,
                        limit: creating.minutesOfWaiting.length
                            ? creating.minutesOfWaiting
                            : undefined,
                    },
                    paramsSerializer: {
                        indexes: null,
                    },
                })
                .then((response) => {
                    if (response.status === 200) {
                        setSendRequestCount(sendRequestCount + 1);
                        setSuccessAdded(true);
                        setModifyPopupOpen(false);
                    } else {
                    }
                })
                .catch((error) => {
                    if (error.status === 400) {
                        alert(error.response.data);
                    }
                });
        }
        setCreatingErrors(errorObject);
    };

    const handleSubmitModify = () => {
        const errorObject: TeamMembersKeys = {};

        if (!isInteger(creating.coordinates.x))
            errorObject['coordinatesX'] = 'Должно быть целым числом';

        if (!isInteger(creating.coordinates.y))
            errorObject['coordinatesY'] = 'Должно быть целым числом';

        if (!isDouble(creating.impactSpeed)) errorObject['impactSpeed'] = 'Должно быть числом';

        if (!isDouble(creating.minutesOfWaiting))
            errorObject['minutesOfWaiting'] = 'Должно быть числом';

        if (!isMaxValue(creating.coordinates.x, 9))
            errorObject['coordinatesX'] = 'Должно быть меньше 9';

        if (!isMaxValue(creating.impactSpeed, 676))
            errorObject['impactSpeed'] = 'Должно быть меньше 676';

        if (!isMinValue(creating.impactSpeed, 0))
            errorObject['impactSpeed'] = 'Должно быть больше или равно 0';

        if (!isMinValue(creating.minutesOfWaiting, 0))
            errorObject['minutesOfWaiting'] = 'Должно быть больше или равно 0';

        setCreatingErrors(errorObject);

        if (Object.keys(errorObject).length === 0) {
            const x: any = {
                name: creating.name.length ? creating.name : undefined,
                coordinates:
                    creating.coordinates.x.length || creating.coordinates.y.length
                        ? {
                              x: creating.coordinates.x.length ? creating.coordinates.x : undefined,
                              y: creating.coordinates.y.length ? creating.coordinates.y : undefined,
                          }
                        : undefined,
                realHero: creating.realHero,
                hasToothpick: creating.hasToothpick,
                impactSpeed: creating.impactSpeed,
                minutesOfWaiting: creating.minutesOfWaiting.length
                    ? creating.minutesOfWaiting
                    : undefined,
                weaponType: creating.weaponType,
                mood: creating.mood,
                carCool: creating.carCool,
            };

            console.log(x);

            if (!modifyObject) return;
            axios
                .put(
                    'https://rwfsh39.ru/s1/api/v1/human-being/' + modifyObject.id,
                    convertToJsonXml(x, 'ModifyHumanBeingRequest'),
                    {headers: {'Content-Type': 'application/xml'}},
                )
                .then((response) => {
                    if (response.status === 200) {
                        setSendRequestCount(sendRequestCount + 1);
                        setSuccessAdded(true);
                        setModifyPopupOpen(false);
                    } else {
                    }
                })
                .catch((error) => {
                    if (error.status === 400) {
                        alert(error.response.data);
                    }
                });
        }
    };

    const handleSubmitCreate = () => {
        const errorObject: TeamMembersKeys = {};

        if (creating.name.length === 0) errorObject['name'] = 'Должно быть не пустым';

        if (!creating.coordinates.x.length || !isInteger(creating.coordinates.x))
            errorObject['coordinatesX'] = 'Должно быть целым числом';

        if (!creating.coordinates.y.length || !isInteger(creating.coordinates.y))
            errorObject['coordinatesY'] = 'Должно быть целым числом';

        if (!creating.impactSpeed.length || !isDouble(creating.impactSpeed))
            errorObject['impactSpeed'] = 'Должно быть числом';

        if (!creating.minutesOfWaiting.length || !isDouble(creating.minutesOfWaiting))
            errorObject['minutesOfWaiting'] = 'Должно быть числом';

        if (!isMaxValue(creating.coordinates.x, 9))
            errorObject['coordinatesX'] = 'Должно быть меньше 9';

        if (!isMaxValue(creating.impactSpeed, 676))
            errorObject['impactSpeed'] = 'Должно быть меньше 676';

        if (!isMinValue(creating.impactSpeed, 0))
            errorObject['impactSpeed'] = 'Должно быть больше или равно 0';

        if (!isMinValue(creating.minutesOfWaiting, 0))
            errorObject['minutesOfWaiting'] = 'Должно быть больше или равно 0';

        setCreatingErrors(errorObject);

        if (Object.keys(errorObject).length === 0) {
            const x: TeamMember = {
                id: 'none',
                name: creating.name,
                coordinates: creating.coordinates,
                creationDate: 'none',
                realHero: creating.realHero,
                hasToothpick: creating.hasToothpick,
                impactSpeed: creating.impactSpeed,
                minutesOfWaiting: creating.minutesOfWaiting,
                weaponType: creating.weaponType,
                mood: creating.mood,
                carCool: creating.carCool,
            };

            axios
                .post(
                    'https://rwfsh39.ru/s1/api/v1/human-being',
                    convertToJsonXml(x, 'CreateHumanBeingRequest'),
                    {headers: {'Content-Type': 'application/xml'}},
                )
                .then((response) => {
                    if (response.status === 200) {
                        setSendRequestCount(sendRequestCount + 1);
                        setSuccessAdded(true);
                        setAddPopupOpen(false);
                    } else {
                    }
                })
                .catch((error) => {
                    if (error.status === 400) {
                        alert(error.response.data);
                    }
                });
        }
    };

    useEffect(() => {
        handleSubmitFilters();
    }, [filters]);

    const moodOptions = ['SORROW', 'APATHY', 'CALM', 'RAGE', 'FRENZY'] as const;
    const weaponTypeOptions = ['HAMMER', 'AXE', 'PISTOL', 'KNIFE', 'BAT'] as const;
    const fieldsOptions = [
        'id',
        'name',
        'coordinateX',
        'coordinateY',
        'creationDate',
        'realHero',
        'hasToothpick',
        'impactSpeed',
        'minutesOfWaiting',
        'carCool',
        'mood',
        'weaponType',
    ] as const;

    const translateFieldOptions = {
        id: 'ИД',
        name: 'Имя',
        coordinateX: 'Координата X',
        coordinateY: 'Координата Y',
        creationDate: 'Дата создания',
        realHero: 'Настоящий герой',
        hasToothpick: 'Есть зубная щетка',
        impactSpeed: 'Скорость',
        minutesOfWaiting: 'Ожидание (мин)',
        carCool: 'Крутая тачка',
        mood: 'Настроение',
        weaponType: 'Оружие',
    } as const;

    useEffect(() => {
        axios
            .get('https://rwfsh39.ru/s1/api/v1/human-being', {
                params: {
                    idGte: filters.idGte,
                    idLte: filters.idLte,
                    nameIn: filters.nameIn
                        ? filters.nameIn.split(',').map((el) => el.trim())
                        : undefined,
                    coordinateXGte: filters.coordinateXGte,
                    coordinateXLte: filters.coordinateXLte,
                    coordinateYGte: filters.coordinateYGte,
                    coordinateYLte: filters.coordinateYLte,
                    creationDateGte: filters.creationDateGte ? filters.creationDateGte : undefined,
                    creationDateLte: filters.creationDateLte ? filters.creationDateGte : undefined,
                    realHero: filters.realHero,
                    hasToothpick: filters.hasToothpick,
                    impactSpeedGte: filters.impactSpeedGte,
                    impactSpeedLte: filters.impactSpeedLte,
                    minutesOfWaitingGte: filters.minutesOfWaitingGte,
                    minutesOfWaitingLte: filters.minutesOfWaitingLte,
                    coolCar: filters.coolCar,
                    moodIn: filters.moodIn,
                    weaponTypeIn: filters.weaponTypeIn,
                    page: filters.page,
                    size: filters.size,
                    sortFields: filters.sortFields,
                    sortDirections: filters.sortDirections,
                },
                paramsSerializer: {
                    indexes: null,
                },
            })
            .then((response) => {
                const parsedResponse = parseAllRequestFromXml(response.data);
                setData(parsedResponse.teamList);
            });
    }, [sendRequestCount]);

    const allowedStatF = ['IMPACT_SPEED', 'MINUTES_OF_WAITING'] as const;
    const allowedStatS = ['AVERAGE', 'MIN', 'MAX'] as const;
    const allowedStatFTranslation = {
        IMPACT_SPEED: 'Скорость удара',
        MINUTES_OF_WAITING: 'Минуты ожидания',
    };
    const allowedStatSTranslation = {AVERAGE: 'Среднее', MIN: 'Минимальное', MAX: 'Максимальное'};

    const [statF, setStatF] = useState<string>('IMPACT_SPEED');
    const [statS, setStatS] = useState<string>('AVERAGE');
    const [statsResponse, setStatsResponse] = useState<string>();

    const calculateStats = () => {
        axios
            .get('https://rwfsh39.ru/s1/api/v1/human-being/stats', {
                params: {
                    field: statF,
                    operation: statS,
                },
            })
            .then((response) => {
                console.log(xml2js(response.data, {compact: true}) as any);
                setStatsResponse(
                    (
                        Math.round(
                            Number(
                                (xml2js(response.data, {compact: true}) as any).StatisticResponseDto
                                    .result._text,
                            ) * 1000,
                        ) / 1000
                    ).toString(),
                );
                // setStatsResponse(xml2js(response.data, {compact: true}));
            });
    };

    return (
        <Wrapper>
            {successAdded && (
                <div style={{width: '100%', marginBottom: 30}}>
                    <Alert
                        theme="success"
                        title="Действие выполнено успешно!"
                        onClose={() => setSuccessAdded(false)}
                    ></Alert>
                </div>
            )}

            <Dialog open={statsPopupOpen} onClose={() => setStatsPopupOpen(false)}>
                <div style={{padding: '40px 30px 30px 30px'}}>
                    <h2 style={{marginBottom: 30, width: '300px'}}>Рассчитать статистику</h2>
                    <div style={{marginBottom: 15, width: '300px'}}>
                        <Select
                            width="max"
                            size="l"
                            value={[statF]}
                            multiple={false}
                            onUpdate={(e) => {
                                setStatF(e[0]);
                            }}
                        >
                            {allowedStatF.map((el) => {
                                return (
                                    <Select.Option
                                        value={el}
                                        content={allowedStatFTranslation[el]}
                                    />
                                );
                            })}
                        </Select>
                    </div>
                    <div style={{marginBottom: 30, width: '100%'}}>
                        <Select
                            width="max"
                            size="l"
                            value={[statS]}
                            multiple={false}
                            onUpdate={(e) => {
                                setStatS(e[0]);
                            }}
                        >
                            {allowedStatS.map((el) => {
                                return (
                                    <Select.Option
                                        value={el}
                                        content={allowedStatSTranslation[el]}
                                    />
                                );
                            })}
                        </Select>
                    </div>
                    <Button onClick={calculateStats}>Рассчитать</Button>
                    {statsResponse && (
                        <div style={{marginTop: 30, fontSize: 16, fontWeight: 300}}>
                            Ответ сервера: {statsResponse}
                        </div>
                    )}
                </div>
            </Dialog>

            <Dialog
                open={openDeleteConfirmPopup}
                onClose={() => {
                    setOpenDeleteConfirmPopup(false);
                }}
            >
                <div style={{padding: '50px 30px 30px 30px'}}>
                    <h1>Подтверждение действия</h1>
                    <p>Вы уверены что хотите удалить данный элемент? ИД: {deleteRequestData?.id}</p>
                    <div
                        style={{
                            display: 'flex',
                            justifyContent: 'flex-end',
                            marginTop: 30,
                            gap: 10,
                        }}
                    >
                        <Button>Отмена</Button>
                        <Button
                            view="action"
                            onClick={() => {
                                axios
                                    .delete('https://rwfsh39.ru/s1/api/v1/human-being/', {
                                        params: {
                                            id: deleteRequestData?.id
                                                ? deleteRequestData?.id
                                                : undefined,
                                        },
                                    })
                                    .then((response) => {
                                        if (response.status === 200) {
                                            setSendRequestCount(sendRequestCount + 1);
                                            setSuccessAdded(true);
                                            setOpenDeleteConfirmPopup(false);
                                        } else {
                                        }
                                    })
                                    .catch((error) => {
                                        if (error.status === 400) {
                                            alert(error.response.data);
                                        }
                                    });
                            }}
                        >
                            Удалить
                        </Button>
                    </div>
                </div>
            </Dialog>
            <Dialog open={addPopupOpen} onClose={() => setAddPopupOpen(false)}>
                <div style={{padding: '30px 30px'}}>
                    <h2>Добавление</h2>
                    <h3 style={{marginBottom: 8}}>Имя</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            value={creating.name}
                            error={creatingErrors['name']}
                            onChange={(e) => handleCreatingChange(e, 'name')}
                        />
                    </div>
                    <h3 style={{marginBottom: 8}}>Координаты</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            placeholder="X"
                            error={creatingErrors.coordinatesX}
                            value={creating.coordinates.x || ''}
                            onChange={(e) => handleCreatingChange(e, 'coordinatesX')}
                        />
                        <TextInput
                            size="l"
                            placeholder="Y"
                            error={creatingErrors.coordinatesY}
                            value={creating.coordinates?.y}
                            onChange={(e) => handleCreatingChange(e, 'coordinatesY')}
                        />
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            checked={creating.realHero ?? false}
                            onUpdate={(e) => handleCreatingChangeS(e, 'realHero')}
                        />
                        <div>Настоящий герой</div>
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            checked={creating.hasToothpick ?? false}
                            onUpdate={(e) => handleCreatingChangeS(e, 'hasToothpick')}
                        />
                        <div>Есть зубная щетка?</div>
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            checked={creating.carCool ?? false}
                            onUpdate={(e) => handleCreatingChangeS(e, 'carCool')}
                        />
                        <div>Крутая тачка?</div>
                    </div>

                    <h3 style={{marginBottom: 8}}>Скорость удара</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            error={creatingErrors.impactSpeed}
                            value={creating.impactSpeed || ''}
                            onChange={(e) => handleCreatingChange(e, 'impactSpeed')}
                        />
                    </div>

                    <h3 style={{marginBottom: 8}}>Минуты ожидания</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            error={creatingErrors['minutesOfWaiting']}
                            value={creating.minutesOfWaiting || ''}
                            onChange={(e) => handleCreatingChange(e, 'minutesOfWaiting')}
                        />
                    </div>
                    <h3 style={{marginBottom: 8}}>Настроение</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Select
                            width="max"
                            size="l"
                            value={[creating.mood]}
                            multiple={false}
                            onUpdate={(e) => handleCreatingChangeS(e[0], 'mood')}
                        >
                            {moodOptions.map((el) => {
                                return (
                                    <Select.Option value={el} content={moodOptionsTranslate[el]} />
                                );
                            })}
                        </Select>
                    </div>
                    <h3 style={{marginBottom: 8}}>Оружие</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Select
                            width="max"
                            size="l"
                            value={[creating.weaponType ?? 'Не выбрано']}
                            onUpdate={(e) => handleCreatingChangeS(e[0], 'weaponType')}
                            multiple={false}
                        >
                            {weaponTypeOptions.map((el) => {
                                return (
                                    <Select.Option
                                        value={el}
                                        content={weaponTypeOptionsTranslate[el]}
                                    />
                                );
                            })}
                        </Select>
                    </div>

                    <Button onClick={handleSubmitCreate} size="l">
                        Добавить
                    </Button>
                </div>
            </Dialog>

            <Dialog open={deletePopupOpen} onClose={() => setDeletePopupOpen(false)}>
                <div style={{padding: '30px 30px', width: 300}}>
                    <h2 style={{marginBottom: 30}}>Удалить по параметрам</h2>

                    <h3 style={{marginBottom: 8}}>Скорость удара</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            error={creatingErrors.impactSpeed}
                            value={creating.impactSpeed || ''}
                            onChange={(e) => handleCreatingChange(e, 'impactSpeed')}
                        />
                    </div>

                    <h3 style={{marginBottom: 8}}>Лимит</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            error={creatingErrors['minutesOfWaiting']}
                            value={creating.minutesOfWaiting || ''}
                            onChange={(e) => handleCreatingChange(e, 'minutesOfWaiting')}
                        />
                    </div>
                    <h3 style={{marginBottom: 8}}>Крутая тачка (+, - или пустое)</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            error={creatingErrors['name']}
                            value={creating.name || ''}
                            onChange={(e) => handleCreatingChange(e, 'name')}
                        />
                    </div>

                    <Button onClick={handleSubmitDelete} size="l">
                        Удалить
                    </Button>
                </div>
            </Dialog>

            <Dialog open={sadPopupOpen} onClose={() => setSadPopupOpen(false)}>
                <div style={{padding: '40px 30px 30px 30px'}}>
                    <h3 style={{marginBottom: 8}}>Номер команды</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            value={team}
                            error={!isInteger(team)}
                            onChange={(e) => setTeam(e.target.value)}
                        />
                    </div>
                    <Button
                        onClick={() => {
                            axios
                                .put(
                                    `https://rwfsh39.ru/s2/api/v1/team/${team}/make-depressive`,
                                    {},
                                    {
                                        headers: {'Content-Type': 'application/xml'},
                                    },
                                )
                                .then((response) => {
                                    if (response.status === 200) {
                                        setSendRequestCount(sendRequestCount + 1);
                                        setSuccessAdded(true);
                                        setSadPopupOpen(false);
                                    } else {
                                    }
                                })
                                .catch((error) => {
                                    if (error.status === 400) {
                                        alert(JSON.stringify(error.response.data).toString());
                                    } else {
                                        alert(JSON.stringify(error.response.data).toString());
                                    }
                                });
                        }}
                    >
                        Сделать грустными
                    </Button>
                </div>
            </Dialog>

            <Dialog open={teamPopupOpen} onClose={() => setTeamPopupOpen(false)}>
                <div style={{padding: '40px 40px 30px 40px'}}>
                    <h3 style={{marginBottom: 8}}>Номер команды</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            value={team}
                            error={!isInteger(team)}
                            onChange={(e) => setTeam(e.target.value)}
                        />
                    </div>
                    <Button
                        onClick={() => {
                            if (!modifyObject) return;
                            axios
                                .post(
                                    `https://rwfsh39.ru/s2/api/v1/team/${team}/add/${modifyObject.id}`,
                                    {},
                                    {headers: {'Content-Type': 'application/xml'}},
                                )
                                .then((response) => {
                                    if (response.status === 200) {
                                        setSendRequestCount(sendRequestCount + 1);
                                        setSuccessAdded(true);
                                        setTeamPopupOpen(false);
                                    } else {
                                    }
                                })
                                .catch((error) => {
                                    if (error.status === 400) {
                                        alert(error.response.data);
                                    }
                                });
                        }}
                    >
                        Добавить в команду
                    </Button>
                </div>
            </Dialog>

            <Dialog open={modifyPopupOpen} onClose={() => setModifyPopupOpen(false)}>
                <div style={{padding: '30px 30px'}}>
                    <h2>Редактирование</h2>
                    <h3 style={{marginBottom: 8}}>Имя</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            value={creating.name}
                            error={creatingErrors['name']}
                            onChange={(e) => handleCreatingChange(e, 'name')}
                        />
                    </div>
                    <h3 style={{marginBottom: 8}}>Координаты</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            placeholder="X"
                            error={creatingErrors.coordinatesX}
                            value={creating.coordinates.x || ''}
                            onChange={(e) => handleCreatingChange(e, 'coordinatesX')}
                        />
                        <TextInput
                            size="l"
                            placeholder="Y"
                            error={creatingErrors.coordinatesY}
                            value={creating.coordinates?.y}
                            onChange={(e) => handleCreatingChange(e, 'coordinatesY')}
                        />
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            checked={creating.realHero ?? false}
                            onUpdate={(e) => handleCreatingChangeS(e, 'realHero')}
                        />
                        <div>Настоящий герой</div>
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            checked={creating.hasToothpick ?? false}
                            onUpdate={(e) => handleCreatingChangeS(e, 'hasToothpick')}
                        />
                        <div>Есть зубная щетка?</div>
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            checked={creating.carCool ?? false}
                            onUpdate={(e) => handleCreatingChangeS(e, 'carCool')}
                        />
                        <div>Крутая тачка?</div>
                    </div>

                    <h3 style={{marginBottom: 8}}>Скорость удара</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            error={creatingErrors.impactSpeed}
                            value={creating.impactSpeed || ''}
                            onChange={(e) => handleCreatingChange(e, 'impactSpeed')}
                        />
                    </div>

                    <h3 style={{marginBottom: 8}}>Минуты ожидания</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            error={creatingErrors['minutesOfWaiting']}
                            value={creating.minutesOfWaiting || ''}
                            onChange={(e) => handleCreatingChange(e, 'minutesOfWaiting')}
                        />
                    </div>
                    <h3 style={{marginBottom: 8}}>Настроение</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Select
                            width="max"
                            size="l"
                            value={[creating.mood]}
                            multiple={false}
                            onUpdate={(e) => handleCreatingChangeS(e[0], 'mood')}
                        >
                            {moodOptions.map((el) => {
                                return (
                                    <Select.Option value={el} content={moodOptionsTranslate[el]} />
                                );
                            })}
                        </Select>
                    </div>
                    <h3 style={{marginBottom: 8}}>Оружие</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Select
                            width="max"
                            size="l"
                            value={[creating.weaponType ?? 'Не выбрано']}
                            onUpdate={(e) => handleCreatingChangeS(e[0], 'weaponType')}
                            multiple={false}
                        >
                            {weaponTypeOptions.map((el) => {
                                return (
                                    <Select.Option
                                        value={el}
                                        content={weaponTypeOptionsTranslate[el]}
                                    />
                                );
                            })}
                        </Select>
                    </div>

                    <Button onClick={handleSubmitModify} size="l">
                        Редактировать
                    </Button>
                </div>
            </Dialog>

            <div
                style={{
                    display: 'flex',
                    width: '100%',
                    gap: 15,
                    alignItems: 'center',
                    marginBottom: 15,
                    justifyContent: 'flex-end',
                }}
            >
                <Button onClick={() => setAddPopupOpen(true)}>Добавить</Button>
                <Button onClick={() => setStatsPopupOpen(true)}>Подсчитать статистику</Button>
                <Button onClick={() => setDeletePopupOpen(true)}>Удалить по параметрам</Button>
                <Button onClick={() => setSadPopupOpen(true)}>
                    Сделать членов команды грустными
                </Button>
            </div>
            <div style={{display: 'flex', gap: 30}}>
                <div style={{width: 300}}>
                    <h2>Фильтры</h2>
                    <h3 style={{marginBottom: 8}}>ID</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            error={filterErrors['idGte']}
                            placeholder="от"
                            value={filters.idGte || ''}
                            onChange={(e) => handleFilterChange(e, 'idGte')}
                        />
                        <TextInput
                            size="l"
                            placeholder="до"
                            error={filterErrors['idLte']}
                            value={filters.idLte || ''}
                            onChange={(e) => handleFilterChange(e, 'idLte')}
                        />
                    </div>
                    <h3 style={{marginBottom: 8}}>Имя (можно несколько через запятую)</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            value={filters.nameIn}
                            error={filterErrors['nameIn']}
                            onChange={(e) => handleFilterChange(e, 'nameIn')}
                        />
                    </div>
                    <h3 style={{marginBottom: 8}}>Координаты</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 8}}>
                        <TextInput
                            size="l"
                            placeholder="X от"
                            error={filterErrors['coordinateXGte']}
                            value={filters.coordinateXGte || ''}
                            onChange={(e) => handleFilterChange(e, 'coordinateXGte')}
                        />
                        <TextInput
                            size="l"
                            placeholder="X до"
                            error={filterErrors['coordinateXLte']}
                            value={filters.coordinateXLte || ''}
                            onChange={(e) => handleFilterChange(e, 'coordinateXLte')}
                        />
                    </div>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            placeholder="Y от"
                            error={filterErrors['coordinateYGte']}
                            value={filters.coordinateYGte || ''}
                            onChange={(e) => handleFilterChange(e, 'coordinateYGte')}
                        />
                        <TextInput
                            size="l"
                            placeholder="Y до"
                            error={filterErrors['coordinateYLte']}
                            value={filters.coordinateYLte || ''}
                            onChange={(e) => handleFilterChange(e, 'coordinateYLte')}
                        />
                    </div>

                    <h3 style={{marginBottom: 8}}>Дата создания</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            placeholder="от (yyyy-mm-dd)"
                            error={filterErrors['creationDateGte']}
                            value={filters.creationDateGte || ''}
                            onChange={(e) => handleFilterChange(e, 'creationDateGte')}
                        />
                        <TextInput
                            size="l"
                            placeholder="до (yyyy-mm-dd)"
                            error={filterErrors['creationDateLte']}
                            value={filters.creationDateLte || ''}
                            onChange={(e) => handleFilterChange(e, 'creationDateLte')}
                        />
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            id="realHeroSwitchFilter"
                            disabled={filters.realHero === null}
                            checked={filters.realHero ?? false}
                            onUpdate={(e) => handleFilterUpdate(e, 'realHero')}
                        />
                        <label
                            htmlFor="realHeroSwitchFilter"
                            style={{opacity: filters.realHero === null ? 0.5 : 1}}
                        >
                            Настоящий герой
                        </label>
                        {filters.realHero === null ? (
                            <LockOpen
                                onClick={() => handleFilterUpdate(false, 'realHero')}
                                style={{cursor: 'pointer'}}
                            />
                        ) : (
                            <CircleXmarkFill
                                onClick={() => handleFilterUpdate(null, 'realHero')}
                                style={{cursor: 'pointer'}}
                            />
                        )}
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            id="hasToothpickSwitchFilter"
                            disabled={filters.hasToothpick === null}
                            checked={filters.hasToothpick ?? false}
                            onUpdate={(e) => handleFilterUpdate(e, 'hasToothpick')}
                        />
                        <label
                            htmlFor="hasToothpickSwitchFilter"
                            style={{opacity: filters.hasToothpick === null ? 0.5 : 1}}
                        >
                            Есть зубная щетка?
                        </label>
                        {filters.hasToothpick === null ? (
                            <LockOpen
                                onClick={() => handleFilterUpdate(false, 'hasToothpick')}
                                style={{cursor: 'pointer'}}
                            />
                        ) : (
                            <CircleXmarkFill
                                onClick={() => handleFilterUpdate(null, 'hasToothpick')}
                                style={{cursor: 'pointer'}}
                            />
                        )}
                    </div>

                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Switch
                            id="coolCarSwitchFilter"
                            disabled={filters.coolCar === null}
                            checked={filters.coolCar ?? false}
                            onUpdate={(e) => handleFilterUpdate(e, 'coolCar')}
                        />
                        <label
                            htmlFor="coolCarSwitchFilter"
                            style={{opacity: filters.coolCar === null ? 0.5 : 1}}
                        >
                            Крутая тачка?
                        </label>
                        {filters.coolCar === null ? (
                            <LockOpen
                                onClick={() => handleFilterUpdate(false, 'coolCar')}
                                style={{cursor: 'pointer'}}
                            />
                        ) : (
                            <CircleXmarkFill
                                onClick={() => handleFilterUpdate(null, 'coolCar')}
                                style={{cursor: 'pointer'}}
                            />
                        )}
                    </div>

                    <h3 style={{marginBottom: 8}}>Скорость удара</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            placeholder="от"
                            error={filterErrors['impactSpeedGte']}
                            value={filters.impactSpeedGte || ''}
                            onChange={(e) => handleFilterChange(e, 'impactSpeedGte')}
                        />
                        <TextInput
                            size="l"
                            placeholder="до"
                            error={filterErrors['impactSpeedLte']}
                            value={filters.impactSpeedLte || ''}
                            onChange={(e) => handleFilterChange(e, 'impactSpeedLte')}
                        />
                    </div>

                    <h3 style={{marginBottom: 8}}>Минуты ожидания</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <TextInput
                            size="l"
                            placeholder="от"
                            error={filterErrors['minutesOfWaitingGte']}
                            value={filters.minutesOfWaitingGte || ''}
                            onChange={(e) => handleFilterChange(e, 'minutesOfWaitingGte')}
                        />
                        <TextInput
                            size="l"
                            placeholder="до"
                            error={filterErrors['minutesOfWaitingLte']}
                            value={filters.minutesOfWaitingLte || ''}
                            onChange={(e) => handleFilterChange(e, 'minutesOfWaitingLte')}
                        />
                    </div>
                    <h3 style={{marginBottom: 8}}>Настроение</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Select
                            width="max"
                            size="l"
                            value={filters.moodIn}
                            multiple={true}
                            onUpdate={(e) => handleFilterUpdateStringA(e, 'moodIn')}
                        >
                            {moodOptions.map((el) => {
                                return (
                                    <Select.Option value={el} content={moodOptionsTranslate[el]} />
                                );
                            })}
                        </Select>
                    </div>
                    <h3 style={{marginBottom: 8}}>Оружие</h3>
                    <div style={{display: 'flex', gap: 15, marginBottom: 15}}>
                        <Select
                            width="max"
                            size="l"
                            value={filters.weaponTypeIn}
                            onUpdate={(e) => handleFilterUpdateStringA(e, 'weaponTypeIn')}
                            multiple={true}
                        >
                            {weaponTypeOptions.map((el) => {
                                return (
                                    <Select.Option
                                        value={el}
                                        content={weaponTypeOptionsTranslate[el]}
                                    />
                                );
                            })}
                        </Select>
                    </div>

                    <h3 style={{marginBottom: 8}}>
                        Сортировка{' '}
                        <Button size="xs" onClick={expandSort}>
                            Добавить поле
                        </Button>
                    </h3>
                    {[...Array(filters.sortFields.length)].map((_, index) => (
                        <div style={{display: 'flex', gap: 15, marginBottom: 8}}>
                            <Select
                                width="max"
                                size="l"
                                multiple={false}
                                value={[filters.sortFields[index] ?? '']}
                                onUpdate={(e) =>
                                    handleFilterUpdateStringInd(e, 'sortFields', index)
                                }
                            >
                                {fieldsOptions.map((el) => {
                                    return (
                                        <Select.Option
                                            value={el}
                                            content={translateFieldOptions[el]}
                                        />
                                    );
                                })}
                            </Select>
                            <Select
                                width="max"
                                size="l"
                                multiple={false}
                                value={[filters.sortDirections[index] ?? '']}
                                onUpdate={(e) =>
                                    handleFilterUpdateStringInd(e, 'sortDirections', index)
                                }
                            >
                                <Select.Option value={'ASC'} content={'По возрастанию'} />
                                <Select.Option value={'DESC'} content={'По убыванию'} />
                            </Select>
                        </div>
                    ))}
                </div>
                <div>
                    <Table
                        data={tableData}
                        columns={columns}
                        emptyMessage="Ничего не найдено"
                    ></Table>

                    <div
                        style={{
                            width: '100%',
                            marginTop: 15,
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'flex-start',
                            gap: 15,
                        }}
                    >
                        <div style={{display: 'flex', alignItems: 'flex-start'}}>
                            <div style={{marginTop: 4}}>Страница</div>
                            <TextInput
                                style={{marginLeft: 15, width: 100}}
                                error={filterErrors['page']}
                                errorPlacement="outside"
                                value={filters.page || ''}
                                onChange={(e) => handleFilterChange(e, 'page')}
                            />
                        </div>
                        <div
                            style={{
                                display: 'flex',
                                alignItems: 'center',
                                gap: 15,
                            }}
                        >
                            <div>Элементов на странице</div>
                            <Select
                                multiple={false}
                                value={[filters.size ?? '10']}
                                onUpdate={(e) => handleFilterUpdateStringA(e, 'size')}
                            >
                                <Select.Option value="1" content="1" />
                                <Select.Option value="5" content="5" />
                                <Select.Option value="10" content="10" />
                                <Select.Option value="20" content="20" />
                                <Select.Option value="50" content="50" />
                            </Select>
                        </div>
                    </div>
                </div>
            </div>
        </Wrapper>
    );
};

export default App;
