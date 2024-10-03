import { Injectable } from '@angular/core';
import { ApiService } from './api-service';

@Injectable({
  providedIn: 'root',
})
export class PdfService {
  constructor(private apiService: ApiService) {}
  downloadPdf(url: string, fileName: string) {
    this.apiService.downloadPdf(url).subscribe({
      next: (blob) => {
        const blobUrl = new Blob([blob], { type: 'application/pdf' });

        // generate a download link
        const url = window.URL.createObjectURL(blobUrl);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${fileName}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Download failed', error);
      },
    });
  }
}
