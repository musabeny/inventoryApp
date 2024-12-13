package sales.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.util.isAllDigits
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.add_item
import inventoryapp.composeapp.generated.resources.cash_in
import inventoryapp.composeapp.generated.resources.keyboard_return
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sales.domain.enums.ClearAction
import sales.presentation.SaleEvent

@Composable
fun CalculatorPage(
    modifier: Modifier,
    enableOperator:Boolean,
    onEvent: (SaleEvent) -> Unit,
    enableCancelBtn:Boolean,
    enableZBtn:Boolean,
    enableAtBtn:Boolean,
    enableDotBtn:Boolean,
    totalCash:String
){
   Column(
       modifier = modifier,
       verticalArrangement = Arrangement.spacedBy(8.dp)
   ) {
      Row(
          modifier = Modifier.weight(5f),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CenteredBtn(
            modifier = Modifier.weight(3f),
            onEvent = onEvent,
            enableOperator = enableOperator,
            enableAtBtn = enableAtBtn,
            enableDotBtn = enableDotBtn
        )
        VerticalBtn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            enableOperator = enableOperator,
            enableCancelBtn = enableCancelBtn,
            onEvent = onEvent
        )
      }
      BottomBtn(
          modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
              .padding(bottom = 10.dp),
          enableOperator = enableOperator,
          onEvent = onEvent,
          totalCash = totalCash,
          enableZBtn = enableZBtn
      )
   }

}

@Composable
fun CenteredBtn(
    modifier: Modifier,
    enableOperator:Boolean,
    enableAtBtn:Boolean,
    enableDotBtn:Boolean,
    onEvent: (SaleEvent) -> Unit,
    showBtn:Boolean = true
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CalculatorButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ,
            color = MaterialTheme.colorScheme.onPrimary,
            labelOne = "@",
            labelTwo = "÷",
            labelThree = "-",
            enableAtBtn = enableAtBtn,
            enableOperator = enableOperator,
            btnOne = {
                onEvent(SaleEvent.SpecialCharacter("@"))
            } ,
            btnTwo = {
                onEvent(SaleEvent.Operator("÷"))
            },
            btnThree = {
                onEvent(SaleEvent.Operator("-"))
            }
        )

        OnlyNumberBtn(
            modifier = Modifier.weight(3f),
            onEvent = onEvent
        )

        CalculatorButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.onPrimary,
            labelOne = ".",
            labelTwo = "0",
            labelThree = "%",
            enableDotBtn = enableDotBtn,
            enableOperator = enableOperator,
            btnOne = {
                onEvent(SaleEvent.SpecialCharacter("."))
            } ,
            btnTwo = {
                onEvent(SaleEvent.Operand("0"))
            },
            btnThree = {
                onEvent(SaleEvent.Operator("%"))
            }
        )
    }
}

@Composable
fun OnlyNumberBtn(
    modifier: Modifier,
    onEvent: (SaleEvent) -> Unit,
    isChangePrice:Boolean = false
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CalculatorButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.onPrimary,
            labelOne = "7",
            labelTwo = "8",
            labelThree = "9",
            enableOperator = true,
            btnOne = {
                onEvent(SaleEvent.Operand("7",isChangePrice))
            } ,
            btnTwo = {
                onEvent(SaleEvent.Operand("8",isChangePrice))
            },
            btnThree = {
                onEvent(SaleEvent.Operand("9",isChangePrice))
            }
        )
        CalculatorButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.onPrimary,
            labelOne = "4",
            labelTwo = "5",
            labelThree = "6",
            enableOperator = true,
            btnOne = {
                onEvent(SaleEvent.Operand("4",isChangePrice))
            } ,
            btnTwo = {
                onEvent(SaleEvent.Operand("5",isChangePrice))
            },
            btnThree = {
                onEvent(SaleEvent.Operand("6",isChangePrice))
            }
        )
        CalculatorButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.onPrimary,
            labelOne = "1",
            labelTwo = "2",
            labelThree = "3",
            enableOperator = true,
            btnOne = {
                onEvent(SaleEvent.Operand("1",isChangePrice))
            } ,
            btnTwo = {
                onEvent(SaleEvent.Operand("2",isChangePrice))
            },
            btnThree = {
                onEvent(SaleEvent.Operand("3",isChangePrice))
            }
        )
    }
}




@Composable
fun BottomBtn(
    modifier: Modifier,
    onEvent: (SaleEvent) -> Unit,
    enableOperator:Boolean,
    totalCash:String,
    enableZBtn:Boolean
){
   Row(
       modifier = modifier,
       horizontalArrangement = Arrangement.spacedBy(8.dp)
   ) {
      CalculatorButton(
          modifier = Modifier
              .weight(1f)
              .fillMaxHeight(),
          color = MaterialTheme.colorScheme.tertiary,
          label = "Z",
          textColor = MaterialTheme.colorScheme.onTertiary,
          onClick = {
              onEvent(SaleEvent.SpecialCharacter("Z"))
          },
          enableBtn = enableZBtn
      )

       CalculatorButton(
           modifier = Modifier
               .weight(3.2f)
               .fillMaxHeight()
               ,
           color = MaterialTheme.colorScheme.secondary,
           label = stringResource(Res.string.cash_in,totalCash),
           textColor = MaterialTheme.colorScheme.onSecondary,
           onClick = {

           }
       )
   }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalBtn(
    modifier: Modifier,
    enableOperator:Boolean,
    onEvent: (SaleEvent) -> Unit,
    enableCancelBtn:Boolean
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        SurfaceButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .alpha(if(enableCancelBtn) 1f else 0.4f)
                .pointerInput(enableCancelBtn){
                    if(!enableCancelBtn) detectTapGestures {  }
                }
                .combinedClickable(
                    onClick = {
                        onEvent(SaleEvent.Clear(ClearAction.SingleCharacter))
                    },
                    onLongClick = {
                        onEvent(SaleEvent.Clear(ClearAction.AllCharacter))
                    }
                )
            ,
            color = MaterialTheme.colorScheme.errorContainer,
            label = "C"
        )


        CalculatorButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onPrimary,
            label = "+",
            onClick = {
                onEvent(SaleEvent.Operator("+"))
            },
            enableBtn =  enableOperator,
        )
        CalculatorButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onPrimary,
            label = "x",
            onClick = {
                onEvent(SaleEvent.Operator("x"))
            },
            enableBtn = enableOperator
        )

        CalculatorButton(
            modifier = Modifier
                .weight(2.1f)
                .fillMaxWidth()
                ,
            color = MaterialTheme.colorScheme.primary,
            label = stringResource(Res.string.add_item),
            textColor = MaterialTheme.colorScheme.onPrimary,
            onClick = {
                onEvent(SaleEvent.AddItem)
            },
            enableBtn = enableOperator
        )
    }
}

@Composable
fun CalculatorButtonRow(
    modifier: Modifier,
    color: Color,
    labelOne:String,
    labelTwo:String,
    enableOperator:Boolean,
    enableAtBtn:Boolean= false,
    enableDotBtn:Boolean= false,
    labelThree:String,
    showIcon:Boolean = false,
    btnOne:() -> Unit ,
    btnTwo:() -> Unit ,
    btnThree:() -> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      CalculatorButton(
          modifier = Modifier
              .weight(1f)
              .fillMaxHeight()
              ,
          color = color,
          label = labelOne,
          onClick = btnOne,
          enableBtn = when(labelOne){
              "@" -> enableAtBtn
              "." -> enableDotBtn
              else -> enableOperator
          }
      )
        CalculatorButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            color = color,
            label = labelTwo,
            onClick = btnTwo,
            enableBtn = if(labelTwo.isAllDigits()) true else enableOperator
        )
        CalculatorButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            color = color,
            label = labelThree,
            onClick = btnThree,
            enableBtn = enableOperator,
            showIcon = showIcon
        )
    }
}

@Composable
fun CalculatorButton(
    modifier: Modifier,
    color: Color,
    label:String,
    textColor:Color = MaterialTheme.colorScheme.primary,
    enableBtn:Boolean = true,
    showIcon:Boolean = false,
    iconBtnColor:Color = MaterialTheme.colorScheme.secondary,
    onClick:() -> Unit,
){
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enableBtn,
        shape = RoundedCornerShape(4.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 2.dp,
        ),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = if(showIcon) iconBtnColor else color,
        )
    ){
        if(showIcon){
            Icon(
                painter = painterResource(Res.drawable.keyboard_return),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }else{
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }


    }
}

@Composable
fun SurfaceButton(
    modifier: Modifier,
    color: Color,
    label:String,
    textColor:Color = MaterialTheme.colorScheme.primary
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 2.dp,
        color = color,
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                ),
            )
        }

    }
}