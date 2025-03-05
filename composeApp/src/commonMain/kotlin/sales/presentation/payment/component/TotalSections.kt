package sales.presentation.payment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_discount
import inventoryapp.composeapp.generated.resources.add_new_item
import inventoryapp.composeapp.generated.resources.add_tax
import inventoryapp.composeapp.generated.resources.choose_payment_mode
import inventoryapp.composeapp.generated.resources.grand_total
import inventoryapp.composeapp.generated.resources.save_for_later
import inventoryapp.composeapp.generated.resources.subtotal
import inventoryapp.composeapp.generated.resources.total_item
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import sales.presentation.components.ActionsButton

@Composable
fun TotalSections(
    modifier: Modifier,
    subTotal:String,
    grandTotal:String,
    totalItem:String
){
    Column(
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.subtotal),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "TSH$subTotal",
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AddButton(
                        modifier = Modifier.weight(1f),
                        label = Res.string.add_tax
                    ){}
                    AddButton(
                        modifier = Modifier.weight(1f),
                        label = Res.string.add_discount
                    ){}
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.grand_total),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "TSH$grandTotal",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.total_item),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = totalItem,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

            }
        }

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            ActionBtn(
//                modifier = Modifier.weight(1f),
//                label = Res.string.save_for_later,
//                textColor = MaterialTheme.colorScheme.primary,
//                containerColor = MaterialTheme.colorScheme.onPrimary
//            ){}
//            ActionBtn(
//                modifier = Modifier.weight(1f),
//                label = Res.string.add_new_item,
//                textColor = MaterialTheme.colorScheme.onPrimary,
//                containerColor = MaterialTheme.colorScheme.primary
//            ){}
//
//        }

        ActionsButton(
            labelOne = Res.string.save_for_later,
            labelTwo = Res.string.add_new_item,
            btnOne = {},
            btnTwo = {}
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = stringResource(Res.string.choose_payment_mode),
            style =  MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.ExtraBold
            ),

        )


    }

}


@Composable
fun AddButton(
    modifier: Modifier,
    label:StringResource,
    onClick:() -> Unit,

){
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp,MaterialTheme.colorScheme.primary),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ){
        Text(
            text = stringResource(label),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ActionBtn(
    modifier: Modifier,
    label:StringResource,
    textColor:Color,
    containerColor:Color,
    onClick:() -> Unit

){
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(2.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = containerColor
        )
    ){
      Text(
          text = stringResource(label),
          style = MaterialTheme.typography.titleSmall.copy(
              color = textColor,
              fontWeight = FontWeight.ExtraBold
          )
      )
    }
}