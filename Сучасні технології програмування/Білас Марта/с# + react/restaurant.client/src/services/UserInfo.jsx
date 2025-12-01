import axios from "axios";

axios.defaults.withCredentials = true;

const BASE_URL = "https://localhost:7135/api/";

export const getUserOrdersByEmail = async (userEmail) => {
  try {
    const response = await axios.get(`${BASE_URL}User/${userEmail}`, {
      withCredentials: true,
    });
    return response;
  } catch (error) {
    console.error("Error during fetching user orders:", error);
  }
};

export const updateUserInfo = async (newInfo) => {
  try {
    const response = await axios.put(`${BASE_URL}User/UpdateUserInfo`, newInfo );
    return response.data;
  } catch (error) {
    console.error(`Error updating user info: ${error}`);
    throw error;
  }
};

export const updateUserPassword = async (newInfo) => {
  try {
    const response = await axios.put(`${BASE_URL}User/UpdateUserPassword`, newInfo );
    return response.data;
  } catch (error) {
    console.error(`Error updating user password: ${error}`);
    throw error;
  }
};
