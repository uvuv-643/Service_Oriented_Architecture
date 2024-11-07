import jakarta.validation.Constraint
import jakarta.validation.Payload
import ru.uvuv643.soa.server.EnumValueValidator
import kotlin.reflect.KClass

@Constraint(validatedBy = [EnumValueValidator::class])
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class EnumValue(
    val enumClass: KClass<out Enum<*>>,
    val message:String = "must be valid enum (check DTO)",
    val groups:Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)