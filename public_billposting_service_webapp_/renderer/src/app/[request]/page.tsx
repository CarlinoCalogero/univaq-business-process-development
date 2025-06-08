'use client';
import { useState, useEffect } from 'react';
import styles from "./page.module.css";
import { PosterAvailabilityRequestResponse } from '@/types/PosterAvailabilityRequestResponse';
import { Bill } from '@/types/Bill';
import { useRouter } from 'next/navigation';
import { Decision } from '@/types/Decision';
import { DEBUG } from '@/config/config';
// import { useParams } from 'next/navigation';

export default function Request() {

  const router = useRouter();

  const [request, setRequest] = useState<PosterAvailabilityRequestResponse | null>(null);
  // const params = useParams();
  // const requestId = params.request;

  useEffect(() => {
    const stored = localStorage.getItem('request');
    if (stored) {
      setRequest(JSON.parse(stored) as PosterAvailabilityRequestResponse);
    }
  }, []);

  const handleDecision = async (isRequestConfirmed: boolean) => {
    if (!request) return

    const decision: Decision = {
      requestId: request.requestId,
      decision: isRequestConfirmed ? 'confirm' : 'cancel'
    };

    let result: Bill = {
      amountDue: null,
      accountHolder: '',
      invoiceNumber: ''
    }

    if (DEBUG) {
      console.log(decision)

      result = {
        amountDue: isRequestConfirmed ? 23.45 : null,
        accountHolder: isRequestConfirmed ? 'MRCUDI2344DJD' : '',
        invoiceNumber: isRequestConfirmed ? '231244353' : ''
      }

      console.log(result)
    } else {

      const res = await fetch('http://localhost:8080/poster-availability/decide', {
        method: 'POST',
        body: JSON.stringify(decision),
        headers: {
          'Content-Type': 'application/json',
        },
      });

      result = await res.json() as Bill;
    }

    // Save in localStorage or a global context
    localStorage.setItem('bill', JSON.stringify(result));

    // Redirect
    router.push(`/${request.requestId}/${isRequestConfirmed ? 'confirm' : 'cancel'}`);
  };

  return (
    <div className={styles.container}>
      {
        request &&
        <div className={styles.innerContainer}>
          <h1>Request #{request.requestId}</h1>
          <table className={styles.zonesTable}>
            <thead>
              <tr>
                <th colSpan={3} className={styles.tableHeader}>Selected Zones</th>
              </tr>
              <tr>
                <th>City</th>
                <th>Zone</th>
                <th>Price (€)</th>
              </tr>
            </thead>
            <tbody>
              {Object.entries(request.selectedZones).map(([city, zones]) =>
                zones.map((zone, index) => (
                  <tr key={`${city}-${zone.name}`} className={styles.zoneCell}>
                    {/* Only show the city name for the first row of each group */}
                    {index === 0 ? (
                      <td rowSpan={zones.length} className={styles.cityCell}>{city}</td>
                    ) : null}
                    <td>{zone.name}</td>
                    <td>{zone.price.toFixed(2)}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      }
      <div className={styles.buttonDiv}>
        <button onClick={() => handleDecision(true)} className={styles.confirmButton}>Confirm</button>
        <button onClick={() => handleDecision(false)} className={styles.cancelButton}>Cancel</button>
      </div>
    </div>
  );
}