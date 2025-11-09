import * as vscode from "vscode";
import { startChallengeCommand } from "./start-challenge.command";
import { submitCodeCommand } from "./submit-code.command";

export function registerCommands(context: vscode.ExtensionContext) {
  context.subscriptions.push(
    vscode.commands.registerCommand(
      "coderunner.startChallenge",
      startChallengeCommand
    )
  );

  context.subscriptions.push(
    vscode.commands.registerCommand("coderunner.submitCode", submitCodeCommand)
  );
}
