async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
    ...options,
  });

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    throw new Error(data?.message || `Request failed (${response.status})`);
  }

  return data;
}

export const api = {
  createProduct: (body) =>
    request("/product-api/api/products", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  createOrder: (body) =>
    request("/order-api/api/orders", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  getOrder: (id) => request(`/order-api/api/orders/${id}`),

  createPayment: (body) =>
    request("/payment-api/api/payments", {
      method: "POST",
      body: JSON.stringify(body),
    }),

  simulatePayment: (transactionCode, success) =>
    request(
      `/payment-api/api/payments/simulate/${encodeURIComponent(transactionCode)}?success=${success}`,
      { method: "POST" },
    ),

  checkHealth: async (prefix) => {
    try {
      const result = await request(`/${prefix}-api/actuator/health`);
      return result?.status === "UP";
    } catch {
      return false;
    }
  },
};
