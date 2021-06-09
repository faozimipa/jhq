export interface ISub {
  id?: number;
  subName?: string;
  code?: string;
  categoryId?: number;
}

export const defaultValue: Readonly<ISub> = {};
