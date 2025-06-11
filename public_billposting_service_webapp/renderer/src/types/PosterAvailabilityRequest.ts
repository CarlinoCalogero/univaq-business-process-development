import { CityPrice } from "./CityPrice"

export type PosterAvailabilityRequest = {
    username: string,
    citiesAndMaximumPrice: CityPrice[],
    posterFormat: string,
    selectionStrategy: string
}