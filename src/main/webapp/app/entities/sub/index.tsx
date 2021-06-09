import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Sub from './sub';
import SubDetail from './sub-detail';
import SubUpdate from './sub-update';
import SubDeleteDialog from './sub-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubDetail} />
      <ErrorBoundaryRoute path={match.url} component={Sub} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubDeleteDialog} />
  </>
);

export default Routes;
