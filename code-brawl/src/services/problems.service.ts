import { apiClient, Problem } from "./api.service";

export async function fetchRandomProblem(): Promise<Problem> {
  const response = await apiClient.get<Problem>("/problem");
  return response.data;
}
