<template>
    <div>
        <p>{{ question }}</p>
        <el-slider v-model="value" :step="1" :min="-8" :max="8" :show-tooltip="false" :marks="marks" :disabled="disabled"
            :height="10" :color="color" :format-tooltip="formatTooltip" @change="handleChange" />
        <p>{{ formatTooltip(value) }}</p>
    </div>
</template>
  
<script>
export default {
    name: "SliderQuestion",
    props: {
        question: {
            type: String,
            required: true,
        },
        disabled: {
            type: Boolean,
            default: false,
        },
        color: {
            type: String,
            default: "#409EFF",
        },
    },
    data() {
        return {
            value: 2,
            marks: {
                1: {
                    label: "不满意",
                    style: { color: "#F56C6C" },
                },
                2: {
                    label: "满意",
                    style: { color: "#67C23A" },
                },
                3: {
                    label: "十分满意",
                    style: { color: "#E6A23C" },
                },
            },
        };
    },
    methods: {
        formatTooltip(value) {
            const map = {
                1: "不满意",
                2: "满意",
                3: "十分满意",
            };
            return map[value];
        },
        handleChange(value) {
            this.$emit("change", value);
        },
    },
};
</script>