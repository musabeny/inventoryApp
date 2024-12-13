package sales.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.enter_price
import org.jetbrains.compose.resources.stringResource
import sales.domain.enums.ClearAction
import sales.presentation.SaleEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPrice(
    modifier: Modifier,
    showDialog:Boolean,
    onEvent: (SaleEvent) -> Unit,
    zType:String?,
    changePrice:String,
    enableDotBtn:Boolean,
    enableEnterBtn:Boolean
){
    if(showDialog){
        BasicAlertDialog(
            onDismissRequest = {
                onEvent(SaleEvent.ShowEnterPriceDialog(false))
            },
            modifier = modifier
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer),
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = stringResource(Res.string.enter_price,zType ?: ""),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            )
                        ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
                            text = changePrice,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Button(
                            modifier = Modifier,
                            onClick = {
                                onEvent(SaleEvent.Clear(
                                    clear = ClearAction.SingleCharacter,
                                    isChangePrice = true
                                ))
                            },
                            shape = RoundedCornerShape(2.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =  MaterialTheme.colorScheme.errorContainer,
                                contentColor =  MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text(
                                text = "C",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                    OnlyNumberBtn(
                        modifier = Modifier
                            .weight(3f)
                            .padding(horizontal = 8.dp),
                        onEvent = onEvent,
                        isChangePrice = true
                    )

                    CalculatorButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 8.dp)
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onPrimary,
                        labelOne = ".",
                        labelTwo = "0",
                        labelThree = "Enter",
                        enableDotBtn = enableDotBtn,
                        enableOperator = enableEnterBtn,
                        showIcon = true,
                        btnOne = {
                            onEvent(SaleEvent.SpecialCharacter(".",true))
                        } ,
                        btnTwo = {
                            onEvent(SaleEvent.Operand("0",true))
                        },
                        btnThree = {

                        }
                    )

                }
            }

        }
    }

}