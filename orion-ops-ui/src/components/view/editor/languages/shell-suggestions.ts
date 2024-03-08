import * as monaco from 'monaco-editor';
import { language as shellLanguage } from 'monaco-editor/esm/vs/basic-languages/shell/shell.js';

const provideCompletionItems = () => {
  const suggestions: any = [];
  shellLanguage.keywords?.forEach((item: any) => {
    suggestions.push({
      label: item,
      kind: monaco.languages.CompletionItemKind.Keyword,
      insertText: item,
    });
  });
  shellLanguage.builtins?.forEach((item: any) => {
    suggestions.push({
      label: item,
      kind: monaco.languages.CompletionItemKind.Function,
      insertText: item,
    });
  });
  return {
    suggestions: [...new Set(suggestions)],
  };
};

export default {
  provideCompletionItems
};
