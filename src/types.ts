enum DownloadStatus {
    Start = 'start',
    Downloading = 'downloading',
    End = 'end',
    Error = 'error',
}

type OnDownloadProgressEvent =
    | {
          status: DownloadStatus.Start;
          progress: number;
      }
    | {
          status: DownloadStatus.Downloading;
          progress: number;
      }
    | {
          status: DownloadStatus.End;
          apkFileName: string;
      }
    | {
          status: DownloadStatus.Error;
          errorMessage: string;
      };

export { OnDownloadProgressEvent, DownloadStatus };
