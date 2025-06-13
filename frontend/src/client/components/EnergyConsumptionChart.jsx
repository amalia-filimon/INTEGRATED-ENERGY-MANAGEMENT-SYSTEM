import React from 'react';
import { Line, Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend
);

const EnergyConsumptionChart = ({ graphicData, chartType }) => {
    const chartData = {
        labels: graphicData.map(data => `${data.minut} min`),
        datasets: [
            {
                label: 'Consumul de energie (kWh)',
                data: graphicData.map(data => data.suma),
                backgroundColor: 'rgba(53, 162, 235, 0.5)',
                borderColor: 'rgb(53, 162, 235)',
                borderWidth: 1,
            },
        ],
    };

    const chartOptions = {
        scales: {
            y: {
                beginAtZero: true,
            },
            x: {
            },
        },
    };

    return (
        <div>
            {chartType === 'line' ? (
                <Line data={chartData} options={chartOptions} />
            ) : (
                <Bar data={chartData} options={chartOptions} />
            )}
        </div>
    );
};

export default EnergyConsumptionChart;
