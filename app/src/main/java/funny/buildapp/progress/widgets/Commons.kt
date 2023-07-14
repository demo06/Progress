package funny.buildapp.progress.widgets

import android.widget.DatePicker
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Sailing
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import funny.buildapp.progress.ui.page.route.BottomNavRoute
import funny.buildapp.progress.ui.page.route.Route
import funny.buildapp.progress.ui.page.route.RouteUtils
import funny.buildapp.progress.ui.page.schedule.PlanBottomSheet
import funny.buildapp.progress.ui.theme.AppTheme
import funny.buildapp.progress.ui.theme.H5
import funny.buildapp.progress.ui.theme.H6
import funny.buildapp.progress.ui.theme.PaddingBorder
import funny.buildapp.progress.ui.theme.ToolBarHeight
import funny.buildapp.progress.ui.theme.ToolBarTitleSize
import funny.buildapp.progress.ui.theme.backgroundColor
import funny.buildapp.progress.ui.theme.black1
import funny.buildapp.progress.ui.theme.grey1
import funny.buildapp.progress.ui.theme.orange
import funny.buildapp.progress.ui.theme.orange1
import funny.buildapp.progress.ui.theme.themeColor
import funny.buildapp.progress.ui.theme.white
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class TabTitle(
    val id: Int,
    val text: String,
    var cachePosition: Int = 0,
    var selected: Boolean = false
)


/**
 * 普通标题栏头部
 */
@Composable
fun AppToolsBar(
    title: String,
    rightText: String? = null,
    onBack: (() -> Unit)? = null,
    onRightClick: (() -> Unit)? = null,
    imageVector: ImageVector? = null,
    tint: Color = AppTheme.colors.mainColor,
    backgroundColor: Color = AppTheme.colors.themeUi,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ToolBarHeight)
            .background(backgroundColor)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            if (onBack != null) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    null,
                    Modifier
                        .clickable(onClick = onBack)
                        .align(Alignment.CenterVertically)
                        .padding(12.dp),
                    tint = tint
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (!rightText.isNullOrEmpty() && imageVector == null) {
                TextContent(
                    text = rightText,
                    color = tint,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 20.dp)
                        .clickWithoutWave { onRightClick?.invoke() }
                )
            }

            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 12.dp)
                        .clickable {
                            onRightClick?.invoke()
                        })
            }
        }
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 40.dp),
            color = tint,
            textAlign = TextAlign.Center,
            fontSize = if (title.length > 14) H5 else ToolBarTitleSize,
            fontWeight = Bold,
            maxLines = 1
        )

    }
}

@Composable
fun BackIcon(modifier: Modifier = Modifier, onBack: () -> Unit) {
    Icon(
        imageVector = Icons.Outlined.ArrowBack,
        contentDescription = null,
        modifier = modifier
            .clickable(onClick = onBack)
            .padding(12.dp),
        tint = AppTheme.colors.icon
    )
}

@Composable
fun NavigationItem(
    modifier: Modifier = Modifier,
    title: String,
    normalIcon: ImageVector,
    pressedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp),
            imageVector = if (isSelected) pressedIcon else normalIcon,
            contentDescription = "icon",
            tint = if (isSelected) themeColor else Color.Gray
        )
        Text(text = title, color = if (isSelected) themeColor else Color.Gray, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutLineEdit(
    modifier: Modifier = Modifier,
    labelStr: String,
    content: String,
    hint: String,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    isPasswordFiled: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressState = interactionSource.collectIsFocusedAsState()
    var text by remember { mutableStateOf(content) }
    OutlinedTextField(
        label = { Text(text = labelStr, color = if (pressState.value) themeColor else grey1) },
        interactionSource = interactionSource,
        visualTransformation = if (isPasswordFiled) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier
            .padding(start = 28.dp, end = 28.dp, top = 30.dp)
            .fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        textStyle = TextStyle(color = if (pressState.value) themeColor else grey1),
        placeholder = { Text(text = hint) },
        leadingIcon = {
            Icon(
                leadingIcon,
                contentDescription = "lock",
                tint = if (pressState.value) themeColor else grey1
            )
        },
        trailingIcon = {
            if (text.isNotEmpty() && trailingIcon != null) {
                Icon(
                    trailingIcon,
                    contentDescription = "close",
                    tint = if (pressState.value) themeColor else grey1
                )
            }

        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = themeColor,
            unfocusedBorderColor = grey1,
            focusedBorderColor = themeColor
        )
    )
}


@Composable
fun RoundCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickWithoutWave { }
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}


@Composable
fun FillWidthButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = themeColor),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun HeadAvatar(
    url: String = "",
    name: String = "No name",
    color: Color = themeColor,
    onClick: () -> Unit
) {
//    val url =
//        "https://lanhu-dds-backend.oss-cn-beijing.aliyuncs.com/merge_image/imgs/61b5e83cabf2490da87aaf09dc293c3d_mergeImage.png"
    if (url.isNotEmpty()) {
        AsyncImage(
            model = url, contentDescription = "", modifier = Modifier
                .clickWithoutWave { onClick() }
                .size(45.dp)
                .clip(
                    CircleShape
                )
        )
    } else {
        Box(
            modifier = Modifier
                .clickWithoutWave { onClick() }
                .size(45.dp)
                .clip(CircleShape)
                .background(color)
        ) {
            Text(
                text = name.first().toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}


@Composable
fun FillWidthEdit(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    onTextChange: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(white, RoundedCornerShape(5.dp))
            .padding(PaddingBorder),
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            value = text,
            onValueChange = onTextChange,
            decorationBox = { innerTextField ->
                if (text.isEmpty())
                    Text(text = hint, color = grey1)
                innerTextField()
            }
        )
    }
}

@Composable
fun KingKongItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    @DrawableRes drawableId: Int? = 0,
    title: String,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickWithoutWave { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (drawableId == 0) {
            Icon(
                modifier = modifier.size(32.dp),
                imageVector = imageVector!!,
                contentDescription = "",
                tint = tint
            )
        } else {
            Image(
                modifier = modifier.size(60.dp),
                painter = painterResource(drawableId!!),
                contentDescription = "",
            )
        }

        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = title,
            fontSize = 12.sp,
        )
    }


}

@Composable
fun NormalCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(AppTheme.colors.card, RoundedCornerShape(4.dp))
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        content()
    }
}

@Composable
fun RoundCornerButton(
    modifier: Modifier = Modifier,
    title: String,
    fontSize: TextUnit = H6,
    fontColor: Color = AppTheme.colors.mainColor,
    onClick: () -> Unit = {}
) {
    Text(
        text = title,
        fontSize = fontSize,
        color = fontColor,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickWithoutWave {
                onClick.invoke()
            }
            .background(
                brush = Brush.horizontalGradient(arrayListOf(orange, orange1)),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 4.dp, horizontal = 16.dp),
    )
}


@Composable
fun FilterMenu(
    modifier: Modifier = Modifier,
    title: String,
    fontSize: TextUnit = H5,
    color: Color = black1,
    @DrawableRes icon: Int,
    onClick: () -> Unit = {}
) {
    TextButton(modifier = modifier, onClick = onClick, shape = RoundedCornerShape(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = title, fontSize = fontSize, color = color, letterSpacing = 0.sp)
            Image(
                painterResource(id = icon),
                contentDescription = "FilterMenu",
            )
        }
    }
}


/**
 * 扩展 LazyColum 嵌套 LazyVerticalGrid
 * @param data 数据
 * @param columnCount 列
 */
fun <T> LazyListScope.gridItems(
    data: List<T>,
    key: ((index: Int) -> Any)? = null,
    columnCount: Int,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    items(rows, key = key) { rowIndex ->
        Row(
            modifier = modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement,
        ) {
            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    if (itemIndex < 0) return@items
                    Box(
                        modifier = Modifier.weight(1F, fill = true),
                        propagateMinConstraints = true
                    ) {
                        itemContent(data[itemIndex])
                    }
                } else {
                    Spacer(Modifier.weight(1F, fill = true))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val confirmEnabled by remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }
    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onConfirm.invoke(
                        datePickerState.selectedDateMillis?.let {
                            val date = Date(it)
                            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            format.format(date)
                        } ?: ""
                    )
                },
                enabled = confirmEnabled
            ) {
                Text(
                    "OK",
                    color = if (confirmEnabled) AppTheme.colors.themeUi else AppTheme.colors.textSecondary
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = AppTheme.colors.background,
        ),
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel", color = AppTheme.colors.themeUi)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                currentYearContentColor = AppTheme.colors.themeUi,
                selectedYearContainerColor = AppTheme.colors.themeUi,
                selectedDayContainerColor = AppTheme.colors.themeUi,
                todayContentColor = AppTheme.colors.themeUi.copy(0.8f),
                todayDateBorderColor = AppTheme.colors.themeUi.copy(0.8f),
            )
        )
    }
}


@Composable
fun SpaceLine() {
    Spacer(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(AppTheme.colors.divider)
    )
}

@Composable
fun BottomBar(navCtrl: NavHostController) {
    val bottomNavList = listOf(BottomNavRoute.Home, BottomNavRoute.Task, BottomNavRoute.Schedule)
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(backgroundColor)
        )
        Row(Modifier.fillMaxWidth()) {
            val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            bottomNavList.forEach { screen ->
                NavigationItem(
                    modifier = Modifier.weight(1f),
                    title = screen.title,
                    normalIcon = screen.normalIcon,
                    pressedIcon = screen.pressIcon,
                    isSelected = currentDestination?.hierarchy?.any { it.route == screen.routeName } == true,
                    onClick = {
                        if (currentDestination?.route != screen.routeName) {
                            navCtrl.navigate(screen.routeName) {
                                popUpTo(navCtrl.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun SwitchButton(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        modifier = modifier
            .semantics { contentDescription = "Demo" },
        checked = checked,
        colors = SwitchDefaults.colors(
            checkedTrackColor = AppTheme.colors.themeUi,
            uncheckedTrackColor = AppTheme.colors.divider,
            uncheckedBorderColor = AppTheme.colors.divider,
        ),
        onCheckedChange = { onCheckedChange(it) })
}

@Composable
fun CustomBottomSheet(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = slideIn { IntOffset(0, it.height / 2) },
        exit = slideOut { IntOffset(0, it.height) }
    ) {
        content()
    }
}

@Preview
@Composable
fun CommonPreview() {

    Column {
        AppToolsBar(title = "标题", rightText = "rightText")
        HeadAvatar {}
        FillWidthEdit(text = "", hint = "请输入内容", onTextChange = {})
        KingKongItem(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.Outlined.Sailing,
            title = "积分商城"
        ) {}
    }


}






