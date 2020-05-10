import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDocumentItem } from 'app/shared/model/document-item.model';

type EntityResponseType = HttpResponse<IDocumentItem>;
type EntityArrayResponseType = HttpResponse<IDocumentItem[]>;

@Injectable({ providedIn: 'root' })
export class DocumentItemService {
  public resourceUrl = SERVER_API_URL + 'api/document-items';

  constructor(protected http: HttpClient) {}

  create(documentItem: IDocumentItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentItem);
    return this.http
      .post<IDocumentItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(documentItem: IDocumentItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentItem);
    return this.http
      .put<IDocumentItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDocumentItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(documentItem: IDocumentItem): IDocumentItem {
    const copy: IDocumentItem = Object.assign({}, documentItem, {
      createdAt: documentItem.createdAt && documentItem.createdAt.isValid() ? documentItem.createdAt.toJSON() : undefined,
      updatedAt: documentItem.updatedAt && documentItem.updatedAt.isValid() ? documentItem.updatedAt.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? moment(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((documentItem: IDocumentItem) => {
        documentItem.createdAt = documentItem.createdAt ? moment(documentItem.createdAt) : undefined;
        documentItem.updatedAt = documentItem.updatedAt ? moment(documentItem.updatedAt) : undefined;
      });
    }
    return res;
  }
}
