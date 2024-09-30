function dateDifference(date) {
    const inputDate = new Date(date);
    const currentDate = new Date();

    inputDate.setHours(0, 0, 0, 0);
    currentDate.setHours(0, 0, 0, 0);

    const timeDifference = currentDate - inputDate;

    const oneDayInMillis = 24 * 60 * 60 * 1000;

    const dayDifference = Math.floor(timeDifference / oneDayInMillis);

    if (dayDifference === 0) {
        return "오늘";
    } else if (dayDifference === 1) {
        return "하루 전";
    } else if (dayDifference > 1) {
        return `${dayDifference}일 전`;
    }
}
