import { createBrowserRouter } from 'react-router-dom'
import UserLayout from '../Layout/UserLayout/UserLayout.jsx';

import Home from '../Pages/Home.jsx';
import Info from '../Pages/Info.jsx';

const UserRouter = createBrowserRouter([
  {
    path: '/',
    element: <UserLayout />,
    children: [
      {
        path: '/',
        element: <Home />,
      },
      {
        path:'/contactInfo',
        element: <Info/>
      }
    ],
  },
])

export default UserRouter;
