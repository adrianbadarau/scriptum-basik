import { Moment } from 'moment';

export interface IDocumentItem {
  id?: number;
  name?: string;
  googleDocsId?: string;
  documentText?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
}

export class DocumentItem implements IDocumentItem {
  constructor(
    public id?: number,
    public name?: string,
    public googleDocsId?: string,
    public documentText?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment
  ) {}
}
