import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISub, defaultValue } from 'app/shared/model/sub.model';

export const ACTION_TYPES = {
  FETCH_SUB_LIST: 'sub/FETCH_SUB_LIST',
  FETCH_SUB: 'sub/FETCH_SUB',
  CREATE_SUB: 'sub/CREATE_SUB',
  UPDATE_SUB: 'sub/UPDATE_SUB',
  DELETE_SUB: 'sub/DELETE_SUB',
  RESET: 'sub/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISub>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SubState = Readonly<typeof initialState>;

// Reducer

export default (state: SubState = initialState, action): SubState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SUB_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUB):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SUB):
    case REQUEST(ACTION_TYPES.UPDATE_SUB):
    case REQUEST(ACTION_TYPES.DELETE_SUB):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SUB_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUB):
    case FAILURE(ACTION_TYPES.CREATE_SUB):
    case FAILURE(ACTION_TYPES.UPDATE_SUB):
    case FAILURE(ACTION_TYPES.DELETE_SUB):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUB_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUB):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUB):
    case SUCCESS(ACTION_TYPES.UPDATE_SUB):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUB):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/subs';

// Actions

export const getEntities: ICrudGetAllAction<ISub> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SUB_LIST,
    payload: axios.get<ISub>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISub> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUB,
    payload: axios.get<ISub>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISub> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUB,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISub> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUB,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISub> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUB,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
