// 主题
export interface TerminalTheme {
  name: string;
  dark: boolean;
  background: string;
  foreground: string;
  cursor: string;
  cursorAccent?: string;
  selectionInactiveBackground?: string;
  selectionBackground?: string;
  selectionForeground?: string;
  black: string;
  red: string;
  green: string;
  yellow: string;
  blue: string;
  magenta: string;
  cyan: string;
  white: string;
  brightBlack: string;
  brightRed: string;
  brightGreen: string;
  brightYellow: string;
  brightBlue: string;
  brightMagenta: string;
  brightCyan: string;
  brightWhite: string;

  [key: string]: unknown;
}

// 默认配色
export const DEFAULT_SCHEMA = {
  name: 'frappe',
  dark: true,
  background: '#303446',
  foreground: '#C6D0F5',
  cursor: '#F2D5CF',
  cursorAccent: '#232634',
  // selectionInactiveBackground: 'rgba(98, 104, 128, 0.30078125)',
  selectionBackground: '#C9DDF0',
  selectionForeground: '#303446',
  black: '#51576D',
  red: '#E78284',
  green: '#A6D189',
  yellow: '#E5C890',
  blue: '#8CAAEE',
  magenta: '#F4B8E4',
  cyan: '#81C8BE',
  white: '#B5BFE2',
  brightBlack: '#626880',
  brightRed: '#E78284',
  brightGreen: '#A6D189',
  brightYellow: '#E5C890',
  brightBlue: '#8CAAEE',
  brightMagenta: '#F4B8E4',
  brightCyan: '#81C8BE',
  brightWhite: '#A5ADCE'
};

export default [
  DEFAULT_SCHEMA,
  {
    name: 'latte',
    dark: false,
    background: '#EFF1F5',
    foreground: '#4C4F69',
    cursor: '#DC8A78',
    cursorAccent: '#EFF1F5',
    // selectionInactiveBackground: 'rgba(172, 176, 190, 0.30078125)',
    selectionForeground: '#EFF1F5',
    selectionBackground: '#6C6F85',
    black: '#5C5F77',
    red: '#D20F39',
    green: '#40A02B',
    yellow: '#DF8E1D',
    blue: '#1E66F5',
    magenta: '#EA76CB',
    cyan: '#179299',
    white: '#ACB0BE',
    brightBlack: '#6C6F85',
    brightRed: '#D20F39',
    brightGreen: '#40A02B',
    brightYellow: '#DF8E1D',
    brightBlue: '#1E66F5',
    brightMagenta: '#EA76CB',
    brightCyan: '#179299',
    brightWhite: '#BCC0CC'
  },
  {
    name: 'macchiato',
    dark: true,
    background: '#24273A',
    foreground: '#CAD3F5',
    cursor: '#F4DBD6',
    cursorAccent: '#181926',
    // selectionInactiveBackground: 'rgba(91, 96, 120, 0.30078125)',
    selectionForeground: '#24273A',
    selectionBackground: '#A5ADCB',
    black: '#494D64',
    red: '#ED8796',
    green: '#A6DA95',
    yellow: '#EED49F',
    blue: '#8AADF4',
    magenta: '#F5BDE6',
    cyan: '#8BD5CA',
    white: '#B8C0E0',
    brightBlack: '#5B6078',
    brightRed: '#ED8796',
    brightGreen: '#A6DA95',
    brightYellow: '#EED49F',
    brightBlue: '#8AADF4',
    brightMagenta: '#F5BDE6',
    brightCyan: '#8BD5CA',
    brightWhite: '#A5ADCB'
  },
  {
    name: 'mocha',
    dark: true,
    background: '#1E1E2E',
    foreground: '#CDD6F4',
    cursor: '#F5E0DC',
    cursorAccent: '#11111B',
    // selectionInactiveBackground: 'rgba(88, 91, 112, 0.30078125)',
    selectionForeground: '#1E1E2E',
    selectionBackground: '#A6ADC8',
    black: '#45475A',
    red: '#F38BA8',
    green: '#A6E3A1',
    yellow: '#F9E2AF',
    blue: '#89B4FA',
    magenta: '#F5C2E7',
    cyan: '#94E2D5',
    white: '#BAC2DE',
    brightBlack: '#585B70',
    brightRed: '#F38BA8',
    brightGreen: '#A6E3A1',
    brightYellow: '#F9E2AF',
    brightBlue: '#89B4FA',
    brightMagenta: '#F5C2E7',
    brightCyan: '#94E2D5',
    brightWhite: '#A6ADC8'
  },
  {
    name: 'AtomOneLight',
    dark: false,
    background: '#F9F9F9',
    foreground: '#2A2C33',
    cursor: '#BBBBBB',
    selectionBackground: '#EDEDED',
    black: '#000000',
    red: '#DE3E35',
    green: '#3F953A',
    yellow: '#D2B67C',
    blue: '#2F5AF3',
    cyan: '#3F953A',
    white: '#BBBBBB',
    brightBlack: '#000000',
    brightRed: '#DE3E35',
    brightGreen: '#3F953A',
    brightYellow: '#D2B67C',
    brightBlue: '#2F5AF3',
    brightCyan: '#3F953A',
    brightWhite: '#FFFFFF'
  },
  {
    name: 'OneHalfDark',
    dark: true,
    background: '#282C34',
    foreground: '#DCDFE4',
    cursor: '#A3B3CC',
    selectionBackground: '#474E5D',
    black: '#282C34',
    red: '#E06C75',
    green: '#98C379',
    yellow: '#E5C07B',
    blue: '#61AFEF',
    cyan: '#56B6C2',
    white: '#DCDFE4',
    brightBlack: '#282C34',
    brightRed: '#E06C75',
    brightGreen: '#98C379',
    brightYellow: '#E5C07B',
    brightBlue: '#61AFEF',
    brightCyan: '#56B6C2',
    brightWhite: '#DCDFE4'
  },
  {
    name: 'dracula',
    dark: true,
    background: '#282A36',
    foreground: '#F8F8F2',
    cursor: '#F8F8F2',
    cursorAccent: '#282A36',
    selectionForeground: '#44475A',
    selectionBackground: '#50FA7B',
    black: '#21222C',
    red: '#FF5555',
    green: '#50FA7B',
    yellow: '#F1FA8C',
    blue: '#BD93F9',
    magenta: '#FF79C6',
    cyan: '#8BE9FD',
    white: '#F8F8F2',
    brightBlack: '#6272A4',
    brightRed: '#FF6E6E',
    brightGreen: '#69FF94',
    brightYellow: '#FFFFA5',
    brightBlue: '#D6ACFF',
    brightMagenta: '#FF92DF',
    brightCyan: '#A4FFFF',
    brightWhite: '#FFFFFF'
  },
  {
    name: 'Solarized Light',
    dark: false,
    background: '#FDF6E3',
    foreground: '#657B83',
    cursor: '#657B83',
    selectionBackground: '#E6DDC3',
    black: '#073642',
    red: '#DC322F',
    green: '#859900',
    yellow: '#B58900',
    blue: '#268BD2',
    cyan: '#2AA198',
    white: '#EEE8D5',
    brightBlack: '#002B36',
    brightRed: '#CB4B16',
    brightGreen: '#586E75',
    brightYellow: '#657B83',
    brightBlue: '#839496',
    brightCyan: '#93A1A1',
    brightWhite: '#FDF6E3'
  },
  {
    name: 'Material Design',
    dark: true,
    background: '#1D262A',
    foreground: '#E7EBED',
    cursor: '#EAEAEA',
    selectionBackground: '#4E6A78',
    black: '#435B67',
    red: '#FC3841',
    green: '#5CF19E',
    yellow: '#FED032',
    blue: '#37B6FF',
    cyan: '#59FFD1',
    white: '#FFFFFF',
    brightBlack: '#A1B0B8',
    brightRed: '#FC746D',
    brightGreen: '#ADF7BE',
    brightYellow: '#FEE16C',
    brightBlue: '#70CFFF',
    brightCyan: '#9AFFE6',
    brightWhite: '#FFFFFF'
  },
  {
    name: 'Duotone Dark',
    dark: true,
    background: '#1F1D27',
    foreground: '#B7A1FF',
    cursor: '#FF9839',
    selectionBackground: '#353147',
    black: '#1F1D27',
    red: '#D9393E',
    green: '#2DCD73',
    yellow: '#D9B76E',
    blue: '#FFC284',
    cyan: '#2488FF',
    white: '#B7A1FF',
    brightBlack: '#353147',
    brightRed: '#D9393E',
    brightGreen: '#2DCD73',
    brightYellow: '#D9B76E',
    brightBlue: '#FFC284',
    brightCyan: '#2488FF',
    brightWhite: '#EAE5FF'
  }
] as Array<TerminalTheme>;
