import type {FormFieldResolverOptions} from "@primevue/forms";
import type {FormErrors} from "@/types/forms.types.ts";
import {toCamelCase} from "@/utils/text.utils.ts";

export type ResolverObject = (options: FormFieldResolverOptions) => {value: string | undefined, errors: FormErrors};
export type FormField = {
    name: string,
    resolver: ResolverObject;
}

export class FormFieldBuilder {
    private readonly fieldName: string;
    private validations: ((options: FormFieldResolverOptions) => boolean)[] = [];
    private errorMessages: string[] = [];

    constructor(fieldName: string) {
        this.fieldName = fieldName;
    }

    public minLength(length: number, errorMessage: string): FormFieldBuilder {
        this.validations.push(({value}: FormFieldResolverOptions): boolean => {
            return value && value.trim().length >= length;
        });
        this.errorMessages.push(errorMessage);
        return this;
    }
    public isNaturalNumber(errorMessage: string): FormFieldBuilder {
        this.validations.push(({value}: FormFieldResolverOptions): boolean => {
            return value && !isNaN(Number(value)) && Number(value) >= 0;
        });
        this.errorMessages.push(errorMessage);
        return this;
    }
    public required(errorMessage: string): FormFieldBuilder {
        this.validations.push(({value}: FormFieldResolverOptions): boolean => {
            return value !== undefined && value.trim() != '';
        });
        this.errorMessages.push(errorMessage);
        return this;
    }

    public build(): FormField {
        return { name: this.fieldName, resolver: this.buildResolver() };
    }

    private buildResolver(): ResolverObject {
        return (options: FormFieldResolverOptions): {value: string | undefined, errors: FormErrors} => {
            const errors: FormErrors = {};
            errors[toCamelCase(this.fieldName)] = [];
            for (let i = 0; i < this.validations.length; ++i) {
                if (!this.validations[i](options)) {
                    errors[toCamelCase(this.fieldName)].push({message: this.errorMessages[i]});
                }
            }
            return {value: options.value, errors};
        };
    }
}