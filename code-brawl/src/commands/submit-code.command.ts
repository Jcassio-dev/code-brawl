import * as vscode from "vscode";
import { submitSolution } from "../services/submit-code.service";

export async function submitCodeCommand() {
  const editor = vscode.window.activeTextEditor;
  if (!editor) {
    vscode.window.showErrorMessage("Nenhum editor de texto ativo.");
    return;
  }

  const code = editor.document.getText();
  const language = editor.document.languageId;

  vscode.window.showInformationMessage(`Enviando código ${language}...`);

  try {
    const result = await submitSolution(code, language);

    if (result.exitCode === 0) {
      vscode.window.showInformationMessage("Sucesso!\n" + result.stdout);
    } else {
      vscode.window.showErrorMessage(
        `Erro (Código: ${result.exitCode}):\n` + result.stderr
      );
    }
  } catch (error: any) {
    console.error(error);
    vscode.window.showErrorMessage(
      "Erro ao conectar no backend: " + error.message
    );
  }
}
