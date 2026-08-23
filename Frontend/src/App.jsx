import { useEffect, useMemo, useState } from "react";
import {
  BadgeCheck,
  Banknote,
  Box,
  Check,
  ChevronRight,
  CircleDollarSign,
  ClipboardList,
  Code2,
  CreditCard,
  LoaderCircle,
  PackagePlus,
  RefreshCw,
  RotateCcw,
  Server,
  ShoppingBag,
  Smartphone,
  X,
} from "lucide-react";
import { api } from "./api";

const services = [
  { key: "product", label: "Product", port: 8082 },
  { key: "order", label: "Order", port: 8083 },
  { key: "payment", label: "Payment", port: 8084 },
];

const steps = [
  { id: 1, label: "Product", icon: Box },
  { id: 2, label: "Order", icon: ShoppingBag },
  { id: 3, label: "Payment", icon: CreditCard },
  { id: 4, label: "Result", icon: BadgeCheck },
];

const paymentMethods = [
  { value: "BANK_TRANSFER", label: "Bank", icon: Banknote },
  { value: "VNPAY", label: "VNPay", icon: CreditCard },
  { value: "MOMO", label: "MoMo", icon: Smartphone },
];

const money = new Intl.NumberFormat("vi-VN", {
  style: "currency",
  currency: "VND",
  maximumFractionDigits: 0,
});

function App() {
  const [step, setStep] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [health, setHealth] = useState({});
  const [activity, setActivity] = useState([]);

  const [productForm, setProductForm] = useState({
    name: "Laptop Dell Demo",
    description: "San pham demo thanh toan",
    price: 15000000,
    quantity: 10,
  });
  const [orderForm, setOrderForm] = useState({
    userId: 1,
    shippingAddress: "Ha Noi",
    quantity: 1,
  });
  const [paymentForm, setPaymentForm] = useState({
    method: "VNPAY",
    bankCode: "VCB",
  });

  const [product, setProduct] = useState(null);
  const [order, setOrder] = useState(null);
  const [payment, setPayment] = useState(null);

  const latestData = useMemo(() => {
    if (payment) return payment;
    if (order) return order;
    if (product) return product;
    return { status: "READY", service: "checkout-demo" };
  }, [product, order, payment]);

  const checkServices = async () => {
    const results = await Promise.all(
      services.map(async (service) => [service.key, await api.checkHealth(service.key)]),
    );
    setHealth(Object.fromEntries(results));
  };

  useEffect(() => {
    checkServices();
  }, []);

  const record = (label, data) => {
    setActivity((current) => [
      { label, data, time: new Date().toLocaleTimeString("vi-VN") },
      ...current,
    ]);
  };

  const run = async (action) => {
    setLoading(true);
    setError("");
    try {
      await action();
    } catch (requestError) {
      setError(requestError.message || "Khong the ket noi backend");
    } finally {
      setLoading(false);
      checkServices();
    }
  };

  const createProduct = () =>
    run(async () => {
      const result = await api.createProduct({
        ...productForm,
        price: Number(productForm.price),
        quantity: Number(productForm.quantity),
      });
      setProduct(result);
      setOrder(null);
      setPayment(null);
      setStep(2);
      record("Product created", result);
    });

  const createOrder = () =>
    run(async () => {
      const result = await api.createOrder({
        userId: Number(orderForm.userId),
        shippingAddress: orderForm.shippingAddress,
        items: [{ productId: product.id, quantity: Number(orderForm.quantity) }],
      });
      setOrder(result);
      setPayment(null);
      setStep(3);
      record("Order created", result);
    });

  const createPayment = () =>
    run(async () => {
      const result = await api.createPayment({
        orderId: order.id,
        method: paymentForm.method,
        bankCode: paymentForm.bankCode,
      });
      setPayment(result);
      setStep(4);
      record("Payment created", result);
    });

  const simulate = (success) =>
    run(async () => {
      const paymentResult = await api.simulatePayment(payment.transactionCode, success);
      const orderResult = await api.getOrder(order.id);
      setPayment(paymentResult);
      setOrder(orderResult);
      record(success ? "Payment approved" : "Payment declined", paymentResult);
      record("Order refreshed", orderResult);
    });

  const reset = () => {
    setStep(1);
    setProduct(null);
    setOrder(null);
    setPayment(null);
    setActivity([]);
    setError("");
  };

  return (
    <div className="app-shell">
      <header className="topbar">
        <div className="brand">
          <div className="brand-mark"><CircleDollarSign size={23} /></div>
          <div>
            <h1>Checkout Lab</h1>
            <span>Microservice console</span>
          </div>
        </div>

        <div className="service-strip">
          {services.map((service) => (
            <div className="service-state" key={service.key}>
              <span className={`status-dot ${health[service.key] ? "online" : "offline"}`} />
              <span>{service.label}</span>
              <small>:{service.port}</small>
            </div>
          ))}
          <button className="icon-button" onClick={checkServices} title="Refresh service status">
            <RefreshCw size={17} />
          </button>
        </div>
      </header>

      <main className="workspace">
        <nav className="step-rail" aria-label="Checkout steps">
          <div className="rail-title">Transaction</div>
          {steps.map(({ id, label, icon: Icon }) => {
            const completed = id < step;
            const active = id === step;
            return (
              <button
                key={id}
                className={`step-button ${active ? "active" : ""} ${completed ? "completed" : ""}`}
                disabled={id > step}
                onClick={() => setStep(id)}
              >
                <span className="step-icon">{completed ? <Check size={17} /> : <Icon size={17} />}</span>
                <span>{label}</span>
                {active && <ChevronRight className="step-arrow" size={16} />}
              </button>
            );
          })}
          <button className="reset-button" onClick={reset}>
            <RotateCcw size={16} /> Reset
          </button>
        </nav>

        <section className="stage">
          <div className="stage-heading">
            <div>
              <span className="eyebrow">Step {step} of 4</span>
              <h2>{steps[step - 1].label}</h2>
            </div>
            <span className="stage-id">TX / {payment?.transactionCode?.slice(-8) || "NEW"}</span>
          </div>

          {error && (
            <div className="error-banner">
              <X size={18} />
              <span>{error}</span>
            </div>
          )}

          {step === 1 && (
            <div className="form-layout">
              <Field label="Product name">
                <input value={productForm.name} onChange={(event) => setProductForm({ ...productForm, name: event.target.value })} />
              </Field>
              <Field label="Description" wide>
                <input value={productForm.description} onChange={(event) => setProductForm({ ...productForm, description: event.target.value })} />
              </Field>
              <Field label="Price (VND)">
                <input type="number" min="1" value={productForm.price} onChange={(event) => setProductForm({ ...productForm, price: event.target.value })} />
              </Field>
              <Field label="Stock">
                <input type="number" min="1" value={productForm.quantity} onChange={(event) => setProductForm({ ...productForm, quantity: event.target.value })} />
              </Field>
              <ActionButton onClick={createProduct} loading={loading} icon={PackagePlus}>
                Create product
              </ActionButton>
            </div>
          )}

          {step === 2 && product && (
            <div className="form-layout">
              <SummaryRow label="Product" value={`#${product.id} · ${product.name}`} wide />
              <SummaryRow label="Unit price" value={money.format(product.price)} />
              <Field label="User ID">
                <input type="number" min="1" value={orderForm.userId} onChange={(event) => setOrderForm({ ...orderForm, userId: event.target.value })} />
              </Field>
              <Field label="Shipping address" wide>
                <input value={orderForm.shippingAddress} onChange={(event) => setOrderForm({ ...orderForm, shippingAddress: event.target.value })} />
              </Field>
              <Field label="Quantity">
                <input type="number" min="1" max={product.quantity} value={orderForm.quantity} onChange={(event) => setOrderForm({ ...orderForm, quantity: event.target.value })} />
              </Field>
              <ActionButton onClick={createOrder} loading={loading} icon={ClipboardList}>
                Create order
              </ActionButton>
            </div>
          )}

          {step === 3 && order && (
            <div className="payment-layout">
              <div className="order-total">
                <span>Order #{order.id}</span>
                <strong>{money.format(order.totalAmount)}</strong>
                <small>{order.status}</small>
              </div>
              <div className="method-section">
                <label>Payment method</label>
                <div className="method-control">
                  {paymentMethods.map(({ value, label, icon: Icon }) => (
                    <button
                      key={value}
                      className={paymentForm.method === value ? "selected" : ""}
                      onClick={() => setPaymentForm({ ...paymentForm, method: value })}
                    >
                      <Icon size={19} /> {label}
                    </button>
                  ))}
                </div>
              </div>
              <Field label="Bank code">
                <select value={paymentForm.bankCode} onChange={(event) => setPaymentForm({ ...paymentForm, bankCode: event.target.value })}>
                  <option value="VCB">Vietcombank</option>
                  <option value="TCB">Techcombank</option>
                  <option value="MB">MB Bank</option>
                  <option value="ACB">ACB</option>
                </select>
              </Field>
              <ActionButton onClick={createPayment} loading={loading} icon={CircleDollarSign}>
                Create payment
              </ActionButton>
            </div>
          )}

          {step === 4 && payment && (
            <div className="result-layout">
              <div className={`result-status ${payment.status === "THANH_CONG" ? "success" : payment.status === "THAT_BAI" ? "failed" : "pending"}`}>
                <div className="result-icon">
                  {payment.status === "THANH_CONG" ? <Check size={30} /> : payment.status === "THAT_BAI" ? <X size={30} /> : <CircleDollarSign size={30} />}
                </div>
                <div>
                  <span>Payment status</span>
                  <h3>{payment.status}</h3>
                </div>
                <strong>{money.format(payment.amount)}</strong>
              </div>
              <div className="transaction-table">
                <SummaryRow label="Transaction" value={payment.transactionCode} wide />
                <SummaryRow label="Method" value={payment.method} />
                <SummaryRow label="Order" value={`#${payment.orderId}`} />
                <SummaryRow label="Order status" value={order?.status || "PENDING_PAYMENT"} />
              </div>
              {payment.status === "CHO_THANH_TOAN" && (
                <div className="decision-bar">
                  <button className="decline-button" disabled={loading} onClick={() => simulate(false)}>
                    <X size={17} /> Decline
                  </button>
                  <button className="approve-button" disabled={loading} onClick={() => simulate(true)}>
                    {loading ? <LoaderCircle className="spin" size={18} /> : <Check size={18} />} Approve
                  </button>
                </div>
              )}
            </div>
          )}
        </section>

        <aside className="inspector">
          <div className="inspector-heading">
            <div><Code2 size={18} /><span>Response</span></div>
            <span>JSON</span>
          </div>
          <pre>{JSON.stringify(latestData, null, 2)}</pre>
          <div className="activity-heading">
            <Server size={17} /> Activity
          </div>
          <div className="activity-list">
            {activity.length === 0 && <span className="empty-state">No requests</span>}
            {activity.map((item, index) => (
              <div className="activity-item" key={`${item.time}-${index}`}>
                <span className="activity-dot" />
                <div><strong>{item.label}</strong><small>{item.time}</small></div>
              </div>
            ))}
          </div>
        </aside>
      </main>
    </div>
  );
}

function Field({ label, wide, children }) {
  return <label className={`field ${wide ? "wide" : ""}`}><span>{label}</span>{children}</label>;
}

function SummaryRow({ label, value, wide }) {
  return <div className={`summary-row ${wide ? "wide" : ""}`}><span>{label}</span><strong>{value}</strong></div>;
}

function ActionButton({ children, icon: Icon, loading, onClick }) {
  return (
    <button className="primary-button" disabled={loading} onClick={onClick}>
      {loading ? <LoaderCircle className="spin" size={18} /> : <Icon size={18} />}
      {children}
    </button>
  );
}

export default App;
