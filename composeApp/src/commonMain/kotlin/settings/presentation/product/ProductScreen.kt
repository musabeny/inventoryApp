package settings.presentation.product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import core.util.UiEvent
import inventoryapp.composeapp.generated.resources.Res
import inventoryapp.composeapp.generated.resources.barcode
import inventoryapp.composeapp.generated.resources.check_circle
import inventoryapp.composeapp.generated.resources.expiry_date
import inventoryapp.composeapp.generated.resources.expiry_date_alert
import inventoryapp.composeapp.generated.resources.itemCode
import inventoryapp.composeapp.generated.resources.itemName
import inventoryapp.composeapp.generated.resources.itemPrice
import inventoryapp.composeapp.generated.resources.preview
import inventoryapp.composeapp.generated.resources.save
import inventoryapp.composeapp.generated.resources.selectColor
import inventoryapp.composeapp.generated.resources.update
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

import settings.domain.enums.CardType
import settings.domain.model.product.ProductColor
import settings.presentation.product.component.CardDetail
import settings.presentation.product.component.SelectDate
import settings.presentation.product.component.TrackExpiry

@Composable
fun ProductScreen(
    state: ProductState,
    onEvent:(ProductEvent) -> Unit,
    navController: NavController,
    snackBarHost: SnackbarHostState,
    uiEvent: Flow<UiEvent>,
    productId:Long
){

    val scrollState  = rememberScrollState()
    val localDensity = LocalDensity.current
    var btnHeight by remember {
        mutableStateOf(0.dp)
    }

    LaunchedEffect( true){
        onEvent(ProductEvent.GetProductById(productId))
        uiEvent.collect{event ->
          when(event){
             is UiEvent.ShowSnackBar ->{
                 snackBarHost.showSnackbar(event.message)
              }
             is UiEvent.PopBackStack ->{
               navController.popBackStack()
             }
              else ->{}
          }
        }

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.BottomCenter,

    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .padding(bottom =btnHeight + 8.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.preview),
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleMedium
            )
            Card(
               modifier = Modifier
                   .fillMaxWidth(0.5f)
                   .height(100.dp)
                ,
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors().copy(
                    containerColor = state.color.color
                )
            ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.BottomEnd
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopStart
                    ){
                        Text(
                            text = state.name,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                  Text(
                      text = state.code,
                      style = MaterialTheme.typography.bodySmall,
                      fontWeight = FontWeight.SemiBold,
                      color = Color.White
                  )
                }
            }

            CardDetail(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary),
                label = Res.string.itemCode,
                onEvent = onEvent,
                errorMessage = state.codeError,
                value = state.code,
                cardType = CardType.CODE,
                itemCodeError = state.itemCodeFoundError
            )
            CardDetail(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary),
                label = Res.string.itemName,
                onEvent = onEvent,
                errorMessage = state.nameError,
                value = state.name,
                cardType = CardType.NAME

            )

            CardDetail(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary),
                label = Res.string.itemPrice,
                cardType = CardType.PRICE,
                onEvent = onEvent,
                errorMessage = state.priceError,
                value =if( state.price == null) "" else "${state.price}",
                priceNotRequired = state.priceNotRequired
            )
            CardDetail(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary),
                label = Res.string.selectColor,
                cardType = CardType.COLOR,
                colorList = state.productColors,
                onEvent = onEvent,

            )
            CardDetail(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary),
                label = Res.string.barcode,
                cardType = CardType.BARCODE,
                onEvent = onEvent,
                value = state.barcode ?: ""
            )
            TrackExpiry(
                modifier = Modifier.fillMaxWidth(),
                onEvent = onEvent,
                selectedDate = state.expireDateFormatted,
                value = state.expireDateAlert ?: ""
            )

        }


        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .onGloballyPositioned {
                      btnHeight = with(localDensity){it.size.height.toDp()}
                }
                .imePadding(),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                onEvent(ProductEvent.AddProduct)
            }
        ){
            Text(
                text = stringResource(if(productId > 0) Res.string.update else Res.string.save),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }

        SelectDate(
            modifier = Modifier,
            showDatePickerDialog = state.showDatePickerDialog,
            onEvent = onEvent
        )



    }
}











