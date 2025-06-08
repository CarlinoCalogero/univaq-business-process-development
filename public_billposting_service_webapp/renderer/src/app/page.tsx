'use client';
import { FormEvent } from 'react';
import styles from "./page.module.css";
import { PosterAvailabilityRequest } from '@/types/PosterAvailabilityRequest';
import { PosterAvailabilityRequestResponse } from '@/types/PosterAvailabilityRequestResponse';
import { useRouter } from 'next/navigation';
import { MutipleSelectInput } from '@/types/MultipleSelectInput';
import MultipleSelectFieldInput from '@/components/MultipleSelectFieldInput';
import { DEBUG } from '@/config/config';


export default function Home() {

  const router = useRouter();

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const formData = new FormData(form);

    const posterAvailabilityRequest: PosterAvailabilityRequest = {
      username: '',
      citiesAndMaximumPrice: [],
      posterFormat: ''
    };

    for (let [key, value] of formData.entries()) {
      switch (key) {
        case 'username':
          posterAvailabilityRequest.username = value as string;
          break;
        case 'cities-entries':
          try {
            const parsed = JSON.parse(value as string) as MutipleSelectInput[];
            parsed.forEach(cityWithMaxPrice => {
              posterAvailabilityRequest.citiesAndMaximumPrice.push({
                city: cityWithMaxPrice.input,
                maxPrice: Number(cityWithMaxPrice.additionalFieldInput)
              });
            });
          } catch (error) {
            console.error('Invalid JSON for cities-entries:', value, error);
          }
          break;
        case 'poster-format':
          posterAvailabilityRequest.posterFormat = value as string;
          break;
      }
    }

    let result: PosterAvailabilityRequestResponse = {
      selectedZones: {},
      requestId: ''
    }

    if (DEBUG) {
      console.log(posterAvailabilityRequest)

      result = {
        selectedZones: {
          "L'Aquila": [
            {
              "id": 6,
              "name": "Flaminio",
              "city": "L'Aquila",
              "price": 19.9
            },
            {
              "id": 1,
              "name": "Center",
              "city": "L'Aquila",
              "price": 12.0
            },
            {
              "id": 3,
              "name": "Coppito",
              "city": "L'Aquila",
              "price": 10.4
            },
            {
              "id": 4,
              "name": "Pettino",
              "city": "L'Aquila",
              "price": 9.9
            },
            {
              "id": 2,
              "name": "Pile",
              "city": "L'Aquila",
              "price": 9.2
            }
          ],
          "Rome": [
            {
              "id": 5,
              "name": "Center",
              "city": "Rome",
              "price": 89.9
            },
            {
              "id": 7,
              "name": "EUR",
              "city": "Rome",
              "price": 38.0
            },
            {
              "id": 9,
              "name": "Trastevere",
              "city": "Rome",
              "price": 29.9
            },
            {
              "id": 8,
              "name": "Parioli",
              "city": "Rome",
              "price": 18.5
            },
            {
              "id": 10,
              "name": "Testaccio",
              "city": "Rome",
              "price": 16.0
            }
          ]
        },
        requestId: "Q06BSjYP86"
      }

      console.log(result)
    } else {

      const res = await fetch('http://localhost:8080/poster-availability/request', {
        method: 'POST',
        body: JSON.stringify(posterAvailabilityRequest),
        headers: {
          'Content-Type': 'application/json',
        },
      });

      result = await res.json() as PosterAvailabilityRequestResponse;

    }

    // Save in localStorage or a global context
    localStorage.setItem('request', JSON.stringify(result));

    // Redirect
    router.push(`/${result.requestId}`);
  };

  return (
    <div className={styles.container}>
      <div className={styles.innerContainer}>
        <h1>Poster Availability Request</h1>
        <form className={styles.formDiv} onSubmit={handleSubmit}>
          <div className={styles.fieldInput}>
            <label htmlFor={'username'}>Username:</label>
            <input
              id='username'
              name='username'
              type="text"
            />
          </div>
          <MultipleSelectFieldInput
            id='cities'
            labelText='City:'
            additionalField
            additionalFieldId='maxPrice'
            additionalFieldLabelText='Max price:'
            additionalFieldLabelPrefixText='&#8364;'
          />
          <MultipleSelectFieldInput
            id='poster-format'
            labelText='Poster Format:'
          />
          <div className={styles.buttonDiv}>
            <input className={styles.submitButton} type="submit" value="Send Request" />
          </div>
        </form>
      </div>
    </div>
  );
}
