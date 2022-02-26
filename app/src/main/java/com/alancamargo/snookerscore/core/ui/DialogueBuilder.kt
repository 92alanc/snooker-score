package com.alancamargo.snookerscore.core.ui

fun makeDialogue(block: Dialogue.() -> Unit): Dialogue {
    return Dialogue().apply(block)
}

fun button(block: Dialogue.ButtonData.() -> Unit): Dialogue.ButtonData {
    return Dialogue.ButtonData().apply(block)
}