import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp


/**
 * Shape [Shapes] definitions used in the application.
 *
 * @property small Shape with rounded corners of 4.dp radius.
 * @property medium Shape with rounded corners of 8.dp radius.
 * @property large Shape with rounded corners of 12.dp radius.
 */
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)
