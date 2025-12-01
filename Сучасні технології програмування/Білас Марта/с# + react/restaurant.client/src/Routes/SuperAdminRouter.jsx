import { createBrowserRouter } from 'react-router-dom'
import Home from '../Pages/Home.jsx';
import Info from '../Pages/Info.jsx';
import { SuperAdminLayout } from '../Layout/SuperAdminLayout/SuperAdminLayout.jsx';

export const SuperAdminRouter = createBrowserRouter([
  {
    path: '/',
    element: <SuperAdminLayout />,
    children: [
      {
        path: '/',
        element: <Home />,
      },
    //   {
    //     path:'/contactInfo',
    //     element: <Info/>
    //   }
    ],
  },
])

