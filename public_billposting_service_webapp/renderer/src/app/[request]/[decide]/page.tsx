'use client';
import { useState, useEffect } from 'react';
import styles from "./page.module.css";
import { Bill } from '@/types/Bill';
import { useRouter } from 'next/navigation';
// import { useParams } from 'next/navigation';


export default function Decide() {

  const router = useRouter();

  const [bill, setBill] = useState<Bill | null>(null);
  // const params = useParams();
  // const requestId = params.request;

  useEffect(() => {
    const stored = localStorage.getItem('bill');
    if (stored) {
      setBill(JSON.parse(stored) as Bill);
    }
  }, []);

  return (
    <div className={styles.container}>
      <div className={styles.innerContainer}>
        {
          bill && bill.amountDue ?
            <div className={styles.innerBillContainer}>
              <h1>Billing info</h1>
              <div className={styles.billingInfoContainer}>
                <div className={styles.billingInfoSingleInfo}>
                  <span className={styles.boldText}>Invoice Number:</span>
                  <span>{bill.invoiceNumber}</span>
                </div>
                <div className={styles.billingInfoSingleInfo}>
                  <span className={styles.boldText}>Account Holder:</span>
                  <span>{bill.accountHolder}</span>
                </div>
                <div className={styles.billingInfoSingleInfo}>
                  <span className={styles.boldText}>Amount Due:</span>
                  <span>&#8364; {bill.amountDue}</span>
                </div>
              </div>
            </div>
            :
            <div>Poster request successfully cancelled!</div>
        }
        <button onClick={() => router.push(`/`)} className={styles.closeButton}>Close</button>
      </div>
    </div>
  );
}