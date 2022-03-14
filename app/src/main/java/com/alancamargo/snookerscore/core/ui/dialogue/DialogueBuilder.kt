package com.alancamargo.snookerscore.core.ui.dialogue

fun makeDialogue(block: Dialogue.() -> Unit): Dialogue {
    return Dialogue().apply(block)
}

fun button(block: Dialogue.ButtonData.() -> Unit): Dialogue.ButtonData {
    return Dialogue.ButtonData().apply(block)
}

fun editText(block: Dialogue.EditTextData.() -> Unit): Dialogue.EditTextData {
    return Dialogue.EditTextData().apply(block)
}

fun radioButtons(block: Dialogue.RadioGroupData.() -> Unit): Dialogue.RadioGroupData {
    return Dialogue.RadioGroupData().apply(block)
}

fun Dialogue.RadioGroupData.radioButton(
    block: Dialogue.RadioButtonData.() -> Unit
): Dialogue.RadioButtonData {
    return Dialogue.RadioButtonData().also {
        block(it)
        radioButtons.add(it)
    }
}
