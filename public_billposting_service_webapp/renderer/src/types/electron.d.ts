export {};

declare global {
  interface Window {
    electronAPI: {
      sendMessage: (channel: string, data: any) => void;
      onMessage: (channel: string, callback: (event: any, ...args: any[]) => void) => void;
    };
  }
}
