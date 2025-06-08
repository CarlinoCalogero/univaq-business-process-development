'use client';
import { useState, useEffect, KeyboardEvent, MouseEvent, ChangeEvent } from 'react';
import { MutipleSelectInput } from '@/types/MultipleSelectInput';
import styles from "./MultipleSelectFieldInput.module.css";

export default function MultipleSelectFieldInput({ id, labelText, additionalField = false, additionalFieldId = '', additionalFieldLabelText = '', additionalFieldLabelPrefixText = '' }: { id: string, labelText: string, additionalField?: boolean, additionalFieldId?: string, additionalFieldLabelText?: string, additionalFieldLabelPrefixText?: string }) {
    const [input, setInput] = useState('');
    const [entries, setEntries] = useState<MutipleSelectInput[]>([]);
    const [serializedEntries, setSerializedEntries] = useState('');

    const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter' && input.trim() !== '') {
            e.preventDefault(); // Prevent form submission if inside a <form>
            setEntries([...entries, {
                input: input.trim(),
                additionalFieldInput: 0
            }]);
            setInput('');
        }
    };

    const removeEntry = (entryIndex: number) => {
        setEntries(prev => prev.filter((_, index) => index !== entryIndex))
    }

    const onAdditionalFieldChange = (event: ChangeEvent<HTMLInputElement>, index: number) => {
        const newValue = Number(event.target.value); // ensure it's a number
        setEntries(prev =>
            prev.map((entry, i) =>
                i === index ? { ...entry, additionalFieldInput: newValue } : entry
            )
        );
    }

    // Update hidden field value when entries change
    useEffect(() => {
        setSerializedEntries(JSON.stringify(entries));
        // console.log(JSON.stringify(entries))
    }, [entries]);

    return (
        <div className={styles.container}>
            <div className={styles.fieldInput}>
                <label htmlFor={id}>{labelText}</label>
                <input
                    id={id}
                    name={additionalField ? "" : id}
                    type="text"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyDown={handleKeyDown}
                />
            </div>
            <input type="hidden" name={additionalField ? `${id}-entries` : ""} value={serializedEntries} />
            <ul className={styles.entriesOuterDiv}>
                {entries.map((entry, index) => (
                    <li key={index}>
                        <div className={styles.entriesDiv}>
                            <div>
                                <p>{entry.input}</p>
                                {additionalField &&
                                    <div className={styles.fieldInput}>
                                        <label htmlFor={additionalFieldId}>{additionalFieldLabelText} {additionalFieldLabelPrefixText && additionalFieldLabelPrefixText}</label>
                                        <input
                                            id={additionalFieldId}
                                            type='number'
                                            value={entry.additionalFieldInput}
                                            onChange={(e) => onAdditionalFieldChange(e, index)}
                                        />
                                    </div>
                                }
                            </div>
                            <button className={styles.singleEntryButton} type="button" onClick={() => removeEntry(index)}>x</button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}
