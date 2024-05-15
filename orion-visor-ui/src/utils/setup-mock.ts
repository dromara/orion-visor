import debug from './env';

export default ({ mock, setup }: { mock?: boolean; setup: () => void }) => {
  if (mock !== false && debug) setup();
};

export const successResponseWrap = (data: unknown) => {
  return {
    data,
    msg: 'success',
    code: 200,
  };
};

export const failResponseWrap = (data: unknown, msg: string, code = 500) => {
  return {
    data,
    msg,
    code,
  };
};
