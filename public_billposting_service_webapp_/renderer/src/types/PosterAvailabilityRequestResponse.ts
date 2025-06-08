import { Zone } from "./Zone"

export type PosterAvailabilityRequestResponse = {
    selectedZones: {
        [city: string]: Zone[]
    },
    requestId: string
}