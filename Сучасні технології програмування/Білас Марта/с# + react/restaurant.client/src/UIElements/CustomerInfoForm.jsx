import React from "react";
import { Row, Col, Button, Form, FloatingLabel } from "react-bootstrap";
import { Formik } from "formik";

import { useAuth } from "../state/Auth/AuthContext";
import {
  CartValidationSchema,
  SignUpValidationSchema,
} from "../utils/validationSchema";
import { OrderDetailsFormFields } from "../components/Order/OrderDetailsFormFields";

const CustomerInfoForm = ({ handleFormSubmit, componentType, errorMessage }) => {
  const { user } = useAuth();

  let schema;
  let initialValues;

  switch (componentType) {
    case "cart":
      schema = CartValidationSchema;
      initialValues = {
        name: user?.firstName || "",
        surname: user?.lastName || "",
        address: user?.address || "",
        phone: user?.phoneNumber || "",
        email: user?.email || "",
        paymentType: "",
        additionInfo: "",
      };
      break;
    case "signUp":
      schema = SignUpValidationSchema;
      initialValues = {
        name: user?.firstName || "",
        surname: user?.lastName || "",
        address: user?.address || "",
        phone: user?.phoneNumber || "",
        email: user?.email || "",
        password: "",
      };
      break;
    default:
  }
  return (
    <Formik
      validationSchema={schema}
      initialValues={initialValues}
      onSubmit={(values) => {
        handleFormSubmit(values);
      }}
    >
      {({ handleSubmit, handleChange, values, errors }) => (
        <Form noValidate onSubmit={handleSubmit}>
          <Row className="pt-3">
            <Col>
              <FloatingLabel controlId="floatingName" label="name">
                <Form.Control
                  type="text"
                  className="custom-input"
                  placeholder="name"
                  name="name"
                  value={values.name}
                  onChange={handleChange}
                  isInvalid={!!errors.name}
                />
              </FloatingLabel>
              <Form.Control.Feedback type="invalid">
                {errors.name}
              </Form.Control.Feedback>
            </Col>
            <Col>
              <FloatingLabel controlId="floatingSurname" label="surname">
                <Form.Control
                  className="custom-input"
                  type="text"
                  placeholder="surname"
                  name="surname"
                  value={values.surname}
                  onChange={handleChange}
                  isInvalid={!!errors.surname}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.surname}
                </Form.Control.Feedback>
              </FloatingLabel>
            </Col>
          </Row>
          <Row className="pt-2">
            <Col>
              <FloatingLabel label="address">
                <Form.Control
                  type="text"
                  className="custom-input"
                  placeholder="address"
                  name="address"
                  value={values.address}
                  onChange={handleChange}
                  isInvalid={!!errors.address}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.address}
                </Form.Control.Feedback>
              </FloatingLabel>
            </Col>
            <Col>
              <FloatingLabel label="phone">
                <Form.Control
                  type="text"
                  className="custom-input"
                  placeholder="phone"
                  name="phone"
                  value={values.phone}
                  onChange={handleChange}
                  isInvalid={!!errors.phone}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.phone}
                </Form.Control.Feedback>
              </FloatingLabel>
            </Col>
          </Row>
          <Row className="pt-2 align-items-end">
            <Col>
              <FloatingLabel label="email">
                <Form.Control
                  type="email"
                  className="custom-input"
                  placeholder="email"
                  name="email"
                  value={values.email}
                  onChange={handleChange}
                  isInvalid={!!errors.email}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.email}
                </Form.Control.Feedback>
              </FloatingLabel>
            </Col>
            {componentType === "cart" ? (
              <Col>
                <Form.Select
                  aria-label="Default select example"
                  placeholder="payment type"
                  className="custom-input"
                  name="paymentType"
                  value={values.paymentType}
                  onChange={handleChange}
                  isInvalid={!!errors.paymentType}
                >
                  <option>payment type</option>
                  <option value="1">cash</option>
                  <option value="2">credit card</option>
                </Form.Select>
                <Form.Control.Feedback type="paymentType">
                  {errors.paymentType}
                </Form.Control.Feedback>
              </Col>
            ) : (
              <Col>
                <FloatingLabel label="password">
                  <Form.Control
                    type="password"
                    className="custom-input"
                    placeholder="password"
                    name="password"
                    value={values.password}
                    onChange={handleChange}
                    isInvalid={!!errors.password}
                  />
                  <Form.Control.Feedback type="invalid">
                    {errors.password}
                  </Form.Control.Feedback>
                </FloatingLabel>
              </Col>
            )}
          </Row>
          {componentType === "cart" && (
            <OrderDetailsFormFields
              handleChange={handleChange}
              values={values}
            />
          )}
           {errorMessage && (
              <Row className="pt-3">
                <Col className="text-danger">
                  <i
                    class="fa-solid fa-circle-exclamation fa-sm pe-2"
                    style={{ color: '#dc3545' }}
                  ></i> 
                    {errorMessage}
                </Col>
              </Row>
            )}
          {componentType === "cart" ? (
            <Row className=" mt-5 pt-5">
              <Col className="d-flex justify-content-end">
                <Button type="submit" className="cartNext-but" size="md">
                  Submit
                </Button>
              </Col>
            </Row>
          ) : (
            <Row className=" mt-5 pt-2">
              <Col className="d-flex justify-content-center">
                <Button type="submit" className="signUp-btn" size="md">
                  Sign up
                </Button>
              </Col>
            </Row>
          )}
        </Form>
      )}
    </Formik>
  );
};

export default CustomerInfoForm;
