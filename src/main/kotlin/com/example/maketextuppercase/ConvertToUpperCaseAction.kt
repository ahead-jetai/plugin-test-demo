package com.example.maketextuppercase

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.ui.Messages

class MakeTextUppercaseAction : AnAction("Make Text Uppercase") {

    override fun actionPerformed(event: AnActionEvent) {
        // Get the editor instance from the event
        val editor: Editor? = event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)
        if (editor == null) {
            // No editor is open
            Messages.showMessageDialog(
                "No editor is open to perform this action.",
                "Action Error",
                Messages.getErrorIcon()
            )
            return
        }

        val selectionModel: SelectionModel = editor.selectionModel
        if (!selectionModel.hasSelection()) {
            // No text is selected
            Messages.showMessageDialog(
                "No text is selected!",
                "Action Info",
                Messages.getInformationIcon()
            )
            return
        }

        val selectedText = selectionModel.selectedText
        if (selectedText.isNullOrEmpty()) {
            // Selected text is somehow null or empty
            Messages.showMessageDialog(
                "Selected text is empty!",
                "Action Info",
                Messages.getInformationIcon()
            )
            return
        }

        // Execute the document modification inside a write command
        WriteCommandAction.runWriteCommandAction(event.project) {
            try {
                val document = editor.document
                document.replaceString(
                    selectionModel.selectionStart,
                    selectionModel.selectionEnd,
                    selectedText.uppercase() // Convert text to uppercase
                )
                // Clear the selection
                selectionModel.removeSelection()
            } catch (e: Exception) {
                // Log or notify any unexpected errors
                Messages.showMessageDialog(
                    "An unexpected error occurred: ${e.message}",
                    "Action Error",
                    Messages.getErrorIcon()
                )
                e.printStackTrace()
            }
        }
    }
}