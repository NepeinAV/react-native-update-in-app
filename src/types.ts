type OnDownloadProgressEvent =
    | {
          status: 'start';
          progress: number;
      }
    | {
          status: 'downloading';
          progress: number;
      }
    | {
          status: 'end';
          apkFileName: string;
      }
    | {
          status: 'error';
          errorMessage: string;
      };

export { OnDownloadProgressEvent };
