import "./App.css";
import { RouterProvider } from "react-router";
import UserRouter from "./Routes/UserRouter";
import { SuperAdminRouter } from "./Routes/SuperAdminRouter";
import { AuthProvider, useAuth } from "./state/Auth/AuthContext";
import "devextreme/dist/css/dx.material.lime.light.css";
import "bootstrap/dist/css/bootstrap.min.css";

const App = () => {
  return (
    <AuthProvider>
      <RouterSelector />
    </AuthProvider>
  );
};

const RouterSelector = () => {
  const { userRole } = useAuth();

  if (userRole == "superAdmin") {
    return <RouterProvider router={SuperAdminRouter} />;
  } else {
    return <RouterProvider router={UserRouter} />;
  }
};

export default App;
