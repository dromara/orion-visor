import type { AxiosResponse } from 'axios';
import type { HttpResponse } from '@/api/interceptor';
import type { UnwrapRef } from 'vue';
import { ref } from 'vue';
import useLoading from './loading';

export default function useRequest<T>(
  api: () => Promise<AxiosResponse<HttpResponse>>,
  defaultValue = [] as unknown as T,
  isLoading = true
) {
  const { loading, setLoading } = useLoading(isLoading);
  const response = ref<T>(defaultValue);
  api().then((res) => {
    response.value = res.data as unknown as UnwrapRef<T>;
  }).finally(() => {
    setLoading(false);
  });
  return { loading, response };
}
