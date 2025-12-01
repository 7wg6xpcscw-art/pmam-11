import { useReducer, createContext, useContext } from "react";
import { orderReducer } from "./OrderReducer";

const initailOrder = { meals: [], total: 0 };
const OrderContext = createContext(initailOrder);
const OrderDispatchContext = createContext(null);

export function OrderProvider({ children }) {
  const [order, dispatch] = useReducer(orderReducer, initailOrder); 
  
  return (
    <OrderContext.Provider value={order}>
      <OrderDispatchContext.Provider value={dispatch}>
        {children}
      </OrderDispatchContext.Provider>
    </OrderContext.Provider>
  );
}

export function useOrder() {
    const order = useContext(OrderContext);
    return order;
}

export function useOrderDispatch(){
    const dispatch = useContext(OrderDispatchContext);
    return dispatch;
}
