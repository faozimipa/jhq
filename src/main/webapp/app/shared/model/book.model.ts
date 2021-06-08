export interface IBook {
  id?: number;
  title?: string;
  isbn?: number;
  author?: string;
  publisher?: string;
  city?: string;
}

export const defaultValue: Readonly<IBook> = {};
