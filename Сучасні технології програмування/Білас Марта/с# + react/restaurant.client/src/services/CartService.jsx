import axios from "axios";

axios.defaults.withCredentials = true;

const BASE_URL = "https://localhost:7135/api/";


export const addMealToCart = async (addModel) => {
  try {
    const response = await axios.post(
      `${BASE_URL}Order/addMealToOrder`,
      addModel,
      {
        withCredentials: true,
      }
    );
    return response;
  } catch (error) {
    console.error("Error adding to cart:", error);
  }
};

export const fetchOrder = async () => {
  try {
    const response = await axios.get(`${BASE_URL}Order/GetOrder`, {
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    console.error(
      "There was an error with the fetch order operation: " + error.message
    );
  }
};

export const decrementMealAmount = async (mealId) => {
  try {
    const response = await axios.put(
      `${BASE_URL}Order/DecrementAmount`,
      mealId,
      {
        headers: {
          "Content-Type": "application/json",
        },
        withCredentials: true,
      }
    );
    return response;
  } catch (error) {
    console.error(
      "here was an error with the fetch decrement meal amount operation: " +
        error.message
    );
  }
};

export const incrementMealAmount = async (mealId) => {
  try {
    const response = await axios.put(
      `${BASE_URL}Order/IncrementAmount`,
      mealId,
      {
        headers: {
          "Content-Type": "application/json",
        },
        withCredentials: true,
      }
    );
    return response;
  } catch (error) {
    console.error(
      "here was an error with the fetch increment meal amount operation: " +
        error.message
    );
  }
};

export const deleteMealFromOrder = async (mealId) => {
  try {
    const response = await axios.delete(`${BASE_URL}Order/deleteOrderRow`, {
      data: JSON.stringify(mealId),
      headers: {
        "Content-Type": "application/json",
      },
      withCredentials: true,
    });
    return response;
  } catch (error) {
    console.error("Error deleting meal:", error);
  }
};

export const placeOrder = async (placeOrderModel) => {
  try {
    const response = await axios.post(
      `${BASE_URL}Order/placeOrder`,
      placeOrderModel,
      {
        withCredentials: true,
      }
    );
    return response;
  } catch (error) {
    console.error("Error place order:" + error);
  }
};
