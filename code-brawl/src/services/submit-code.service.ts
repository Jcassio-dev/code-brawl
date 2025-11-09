import { apiClient, ExecutionResult } from "./api.service";

export async function submitSolution(
  code: string,
  language: string
): Promise<ExecutionResult> {
  const response = await apiClient.post<ExecutionResult>("/submit", {
    code: code,
    language: language,
  });
  return response.data;
}
