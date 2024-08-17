package settings.presentation.product.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.component.CustomTextField
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.barcode
import inventoryapp.composeapp.generated.resources.check_circle
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import settings.domain.enums.CardType
import settings.domain.model.product.ProductColor
import settings.presentation.product.ProductEvent

@OptIn( ExperimentalLayoutApi::class)
@Composable
fun CardDetail(
    modifier: Modifier,
    label: StringResource,
    cardType: CardType = CardType.CODE,
    colorList:List<ProductColor> = emptyList(),
    onEvent: (ProductEvent) -> Unit,
    errorMessage: StringResource? = null,
    value: String = "",
    priceNotRequired:Boolean = false,
    itemCodeError: StringResource? = null
){

    Surface(
        modifier = modifier,
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .background(MaterialTheme.colorScheme.onPrimary),
            verticalArrangement = Arrangement.spacedBy(4.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                AnimatedVisibility(visible = itemCodeError != null ){
                    itemCodeError?.let {
                        Text(
                            text = stringResource(it),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                }
            }


            when(cardType){
                CardType.PRICE ->{
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if(priceNotRequired){
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "?",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }else{
                            CustomTextField(
                                modifier = Modifier.weight(1f),
                                onValue = {
                                    onEvent(ProductEvent.EnterItemPrice(it))
                                },
                                errorMessage = errorMessage,
                                value = value,
                                keyboardType = KeyboardType.Number
                            )
                        }

                        Row(
                            modifier = Modifier.weight(2f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                onCheckedChange = {
                                    onEvent(ProductEvent.IsPriceRequired)
                                },
                                checked = priceNotRequired,
                                colors = CheckboxDefaults.colors().copy(
                                    uncheckedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "Enter Price During Billing",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                    }
                }
                CardType.COLOR ->{
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 4
                    ) {
                        colorList.forEach{color ->
                            Box(
                                modifier = Modifier.clickable {
                                    onEvent(ProductEvent.SelectColor(color))
                                },
                                contentAlignment = Alignment.Center
                            ){
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(
                                            shape = CircleShape,
                                            color = color.color
                                        )
                                )
                                if(color.isSelected){
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(Res.drawable.check_circle),
                                        contentDescription = "check icon",
                                        tint = Color.White
                                    )
                                }


                            }

                        }
                    }
                }
                CardType.BARCODE ->{
                    Box( modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp, bottom = 8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Click to Scan",
                                style = MaterialTheme.typography.labelSmall,
//                           color = MaterialTheme.colorScheme.surfaceBright
                            )
                            Icon(
                                painter = painterResource(Res.drawable.barcode),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                }
                else ->{
                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage  = errorMessage,
                        onValue = {
                            when(cardType){
                                CardType.CODE ->onEvent(ProductEvent.EnterItemCode(it))
                                CardType.NAME ->onEvent(ProductEvent.EnterItemName(it))
                                else ->{}
                            }
                        },
                        value = value,
                        keyboardType = if(cardType == CardType.CODE) KeyboardType.Number else KeyboardType.Text
                    )
                }
            }


        }
    }
}