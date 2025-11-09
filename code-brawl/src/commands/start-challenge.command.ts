import * as vscode from "vscode";
import { fetchRandomProblem } from "../services/problems.service";

export async function startChallengeCommand() {
  vscode.window.showInformationMessage("Buscando um novo desafio...");

  try {
    const problem = await fetchRandomProblem();

    const content =
      `/*\n * Desafio: ${problem.title}\n * Descrição: ${problem.description}\n */\n\n` +
      problem.boilerplate;

    const doc = await vscode.workspace.openTextDocument({
      content: content,
      language: "javascript",
    });

    await vscode.window.showTextDocument(doc, vscode.ViewColumn.One);
  } catch (error: any) {
    console.error(error);
    vscode.window.showErrorMessage("Erro ao buscar desafio: " + error.message);
  }
}
