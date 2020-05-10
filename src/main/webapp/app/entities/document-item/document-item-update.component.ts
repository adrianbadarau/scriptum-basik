import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDocumentItem, DocumentItem } from 'app/shared/model/document-item.model';
import { DocumentItemService } from './document-item.service';

@Component({
  selector: 'jhi-document-item-update',
  templateUrl: './document-item-update.component.html'
})
export class DocumentItemUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    googleDocsId: [null, [Validators.required]],
    documentText: [null, [Validators.required]],
    createdAt: [],
    updatedAt: [null, [Validators.required]]
  });

  constructor(protected documentItemService: DocumentItemService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentItem }) => {
      if (!documentItem.id) {
        const today = moment().startOf('day');
        documentItem.createdAt = today;
        documentItem.updatedAt = today;
      }

      this.updateForm(documentItem);
    });
  }

  updateForm(documentItem: IDocumentItem): void {
    this.editForm.patchValue({
      id: documentItem.id,
      name: documentItem.name,
      googleDocsId: documentItem.googleDocsId,
      documentText: documentItem.documentText,
      createdAt: documentItem.createdAt ? documentItem.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: documentItem.updatedAt ? documentItem.updatedAt.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentItem = this.createFromForm();
    if (documentItem.id !== undefined) {
      this.subscribeToSaveResponse(this.documentItemService.update(documentItem));
    } else {
      this.subscribeToSaveResponse(this.documentItemService.create(documentItem));
    }
  }

  private createFromForm(): IDocumentItem {
    return {
      ...new DocumentItem(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      googleDocsId: this.editForm.get(['googleDocsId'])!.value,
      documentText: this.editForm.get(['documentText'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentItem>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
