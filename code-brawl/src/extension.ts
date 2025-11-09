import * as vscode from "vscode";
import axios from "axios";

const API_URL = "http://localhost:8080/api/submit";

export function activate(context: vscode.ExtensionContext) {
  console.log('Parabéns, sua extensão "coderunner" está ativa!');

  let disposable = vscode.commands.registerCommand(
    "coderunner.submitCode",
    async () => {
      const editor = vscode.window.activeTextEditor;
      if (!editor) {
        vscode.window.showErrorMessage("Nenhum editor de texto ativo.");
        return;
      }

      const code = editor.document.getText();
      const language = editor.document.languageId;

      vscode.window.showInformationMessage(`Enviando código ${language}...`);

      try {
        const response = await axios.post(API_URL, {
          code: code,
          language: language,
        });

        const result = response.data;

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
  );

  context.subscriptions.push(disposable);
}

export function deactivate() {}
