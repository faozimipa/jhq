import { ISub } from 'app/shared/model/sub.model';

export interface ICategory {
  id?: number;
  categoryName?: string;
  subs?: ISub[];
}

export const defaultValue: Readonly<ICategory> = {};
