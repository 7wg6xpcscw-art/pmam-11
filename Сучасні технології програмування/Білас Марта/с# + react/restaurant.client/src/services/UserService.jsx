import axios from "axios";

axios.defaults.withCredentials = true;

const BASE_URL = "https://localhost:7135/api/";

export const signIn = async (logInModel) => {
    try {
      const response = await axios.post(
        `${BASE_URL}User/signIn`,
        JSON.stringify(logInModel),
        {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true,
        }
      );
      return response;
    } catch (error) {
      if (error.response) {
        console.error("Error during sign in:", error.response.data.message);
      } else if (error.request) {
        console.error("No response received:", error.message);
      } else {
        console.error("Error", error.message);
      }
    }
  };
  export const signUp = async (createUserModel) => {
    try {
      const response = await axios.post(
        `${BASE_URL}User/signUp`,
        JSON.stringify(createUserModel),
        {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true,
        }
      );
      return response;
    } catch (error) {
      console.error("Error during sign up:", error);
    }
  };
  
  export const logOut = async () => {
    try {
      const response = await axios.get(`${BASE_URL}User/logOut`, {
        withCredentials: true,
      });
      return response;
    } catch (error) {
      console.error("Error during log out:", error);
    }
  };