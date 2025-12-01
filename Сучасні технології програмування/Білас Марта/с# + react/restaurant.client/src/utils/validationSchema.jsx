import * as yup from "yup";

const baseSchema = {
  name: yup
    .string()
    .required("Name is required")
    .matches(/^[A-Za-z-]+$/, "Name must contain only letters and -"),
  surname: yup
    .string()
    .required("Surname is required")
    .matches(/^[A-Za-z-]+$/, "Surname must contain only letters and -"),
  address: yup.string().required("Address is required"),
  phone: yup
    .string()
    .required("Phone is required")
    .matches(/^\d+$/, "Phone must contain only numbers")
    .max(13, "Phone number must be at most 13 characters long"),
  email: yup
    .string()
    .email("Invalid email address")
    .required("Email is required"),
};

export const UserInfoValidationSchema = () => {
  return yup.object({
    ...baseSchema
  });
};
export const CartValidationSchema = yup.object().shape({
  ...baseSchema,
  paymentType: yup.string().required("Please select an option"),
});

export const SignUpValidationSchema = yup.object().shape({
  ...baseSchema,
  password: yup
    .string()
    .min(6, "Password must be at least 6 characters")
    .required("Password is required"),
});

export const SignInValidationSchema = () => {
  return yup.object({
    email: yup
      .string()
      .email("Invalid email address")
      .required("Email is required"),
    password: yup.string().required("Password is required"),
  });
};


export const UserPasswordValidationSchema = yup.object({
  oldPassword: yup.string().required("Enter your old password"),
  newPassword: yup
    .string()
    .min(6, "Password must be at least 6 characters")
    .required("Enter new password"),
  confirmNewPassword: yup
    .string()
    .oneOf([yup.ref("newPassword")], "Passwords must match")
    .required("Confirm your new password "),
});


