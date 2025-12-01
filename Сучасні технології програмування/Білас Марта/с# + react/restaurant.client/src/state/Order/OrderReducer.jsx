export const orderReducer = (state, action) => {
  switch (action.type) {
    case "add": {
      const existingMealIndex = state.meals.findIndex(
        (meal) => meal.id === action.meal.id
      );
      if (existingMealIndex >= 0) {
        const newMeals = [...state.meals];
        newMeals[existingMealIndex].amount += action.meal.amount;
        const newTotal = newMeals.reduce(
          (total, meal) => total + meal.price * meal.amount,
          0
        );
        return { meals: newMeals, total: newTotal };
      } else {
        const newMeals = [...state.meals, action.meal];
        const newTotal = newMeals.reduce(
          (total, meal) => total + meal.price * meal.amount,
          0
        );
        const newOrder = { meals: newMeals, total: newTotal };
        return newOrder;
      }
    }
    case "changeAmount": {
      const mealIndexToChange = state.meals.findIndex(
        (meal) => meal.id === action.id
      );
      if (mealIndexToChange >= 0) {
        const newMeals = [...state.meals];
        newMeals[mealIndexToChange].amount = action.amount;
        const newTotal = newMeals.reduce(
          (total, meal) => total + meal.price * meal.amount,
          0
        );
        return { meals: newMeals, total: newTotal };
      } else {
        return state;
      }
    }
    case "delete": {
      const mealIndexToDelete = state.meals.findIndex(
        (meal) => meal.id === action.id
      );
      if (mealIndexToDelete >= 0) {
        const newMeals = [...state.meals];
        const deletedMeal = newMeals.splice(mealIndexToDelete, 1)[0];
        const newTotal = state.total - deletedMeal.price * deletedMeal.amount;
        return { meals: newMeals, total: newTotal };
      } else {
        return state;
      }
    }
    case "clear": {
      return { meals: [], total: 0 };
    }
    default: {
      throw Error("Unknown action: " + action.type);
    }
  }
};
