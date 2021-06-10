export interface ISub {
  id?: number;
  subName?: string;
  code?: string;
  categoryId?: number;
  categoryName?: string;
}

export const defaultValue: Readonly<ISub> = {};
